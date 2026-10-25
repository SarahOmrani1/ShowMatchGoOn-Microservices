from fastapi import FastAPI, HTTPException, status
from pydantic import BaseModel, Field
from typing import List, Optional
import py_eureka_client.eureka_client as eureka_client
from contextlib import asynccontextmanager
from datetime import datetime
import sqlite3

PORT = 8082
EUREKA_SERVER = "http://localhost:8761/eureka"

def init_db():
    conn = sqlite3.connect("moderation.db")
    cursor = conn.cursor()
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS reports (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            reported_user_id INTEGER NOT NULL,
            reported_by_id INTEGER NOT NULL,
            reason TEXT NOT NULL,
            description TEXT,
            status TEXT DEFAULT 'PENDING',
            created_at TEXT
        )
    """)
    cursor.execute("""
        CREATE TABLE IF NOT EXISTS sanctions (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id INTEGER NOT NULL,
            sanction_type TEXT NOT NULL,
            reason TEXT NOT NULL,
            duration_days INTEGER DEFAULT 0,
            applied_at TEXT
        )
    """)
    conn.commit()
    conn.close()

@asynccontextmanager
async def lifespan(app: FastAPI):
    init_db()
    await eureka_client.init_async(
        eureka_server=EUREKA_SERVER,
        app_name="MODERATION-SERVICE",
        instance_port=PORT,
        instance_host="localhost",
        status_page_url=f"http://localhost:{PORT}/docs"
    )
    yield
    await eureka_client.stop_async()

app = FastAPI(
    title="Microservice Modération & Sanctions",
    description="Gestion des signalements et des sanctions utilisateurs",
    version="1.0.0",
    lifespan=lifespan
)

class ReportCreate(BaseModel):
    reported_user_id: int = Field(..., example=1)
    reported_by_id: int = Field(..., example=2)
    reason: str = Field(..., example="Spam dans le chat")
    description: Optional[str] = Field(None, example="Envoie des liens suspects")

class ReportUpdateStatus(BaseModel):
    status: str = Field(..., example="RESOLVED")

class ReportResponse(BaseModel):
    id: int
    reported_user_id: int
    reported_by_id: int
    reason: str
    description: Optional[str]
    status: str
    created_at: str

class SanctionCreate(BaseModel):
    user_id: int = Field(..., example=1)
    sanction_type: str = Field(..., example="TEMPORARY_BAN")
    reason: str = Field(..., example="Comportement inapproprié répété")
    duration_days: int = Field(0, example=7)

class SanctionResponse(BaseModel):
    id: int
    user_id: int
    sanction_type: str
    reason: str
    duration_days: int
    applied_at: str

@app.post("/api/reports", response_model=ReportResponse, status_code=201, tags=["Signalements"])
def create_report(report: ReportCreate):
    """Créer un nouveau signalement."""
    conn = sqlite3.connect("moderation.db")
    cursor = conn.cursor()
    created_at = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    cursor.execute("""
        INSERT INTO reports (reported_user_id, reported_by_id, reason, description, status, created_at)
        VALUES (?, ?, ?, ?, 'PENDING', ?)
    """, (report.reported_user_id, report.reported_by_id, report.reason, report.description, created_at))
    report_id = cursor.lastrowid
    conn.commit()
    conn.close()
    return ReportResponse(id=report_id, reported_user_id=report.reported_user_id,
        reported_by_id=report.reported_by_id, reason=report.reason,
        description=report.description, status="PENDING", created_at=created_at)

@app.get("/api/reports", response_model=List[ReportResponse], tags=["Signalements"])
def get_all_reports():
    """Récupérer tous les signalements."""
    conn = sqlite3.connect("moderation.db")
    cursor = conn.cursor()
    cursor.execute("SELECT id, reported_user_id, reported_by_id, reason, description, status, created_at FROM reports")
    rows = cursor.fetchall()
    conn.close()
    return [ReportResponse(id=r[0], reported_user_id=r[1], reported_by_id=r[2],
        reason=r[3], description=r[4], status=r[5], created_at=r[6]) for r in rows]

@app.get("/api/reports/{id}", response_model=ReportResponse, tags=["Signalements"])
def get_report_by_id(id: int):
    """Récupérer un signalement par ID."""
    conn = sqlite3.connect("moderation.db")
    cursor = conn.cursor()
    cursor.execute("SELECT id, reported_user_id, reported_by_id, reason, description, status, created_at FROM reports WHERE id = ?", (id,))
    row = cursor.fetchone()
    conn.close()
    if not row:
        raise HTTPException(status_code=404, detail="Signalement introuvable.")
    return ReportResponse(id=row[0], reported_user_id=row[1], reported_by_id=row[2],
        reason=row[3], description=row[4], status=row[5], created_at=row[6])

@app.put("/api/reports/{id}/status", tags=["Signalements"])
def update_report_status(id: int, update: ReportUpdateStatus):
    """Mettre à jour le statut d'un signalement."""
    conn = sqlite3.connect("moderation.db")
    cursor = conn.cursor()
    cursor.execute("UPDATE reports SET status = ? WHERE id = ?", (update.status, id))
    conn.commit()
    updated = cursor.rowcount
    conn.close()
    if updated == 0:
        raise HTTPException(status_code=404, detail="Signalement introuvable.")
    return {"message": "Statut mis à jour", "new_status": update.status}

@app.delete("/api/reports/{id}", status_code=204, tags=["Signalements"])
def delete_report(id: int):
    """Supprimer un signalement."""
    conn = sqlite3.connect("moderation.db")
    cursor = conn.cursor()
    cursor.execute("DELETE FROM reports WHERE id = ?", (id,))
    conn.commit()
    deleted = cursor.rowcount
    conn.close()
    if deleted == 0:
        raise HTTPException(status_code=404, detail="Signalement introuvable.")

@app.post("/api/sanctions", response_model=SanctionResponse, status_code=201, tags=["Sanctions"])
def apply_sanction(sanction: SanctionCreate):
    """Appliquer une sanction à un utilisateur."""
    conn = sqlite3.connect("moderation.db")
    cursor = conn.cursor()
    applied_at = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    cursor.execute("""
        INSERT INTO sanctions (user_id, sanction_type, reason, duration_days, applied_at)
        VALUES (?, ?, ?, ?, ?)
    """, (sanction.user_id, sanction.sanction_type, sanction.reason, sanction.duration_days, applied_at))
    sanction_id = cursor.lastrowid
    conn.commit()
    conn.close()
    return SanctionResponse(id=sanction_id, user_id=sanction.user_id,
        sanction_type=sanction.sanction_type, reason=sanction.reason,
        duration_days=sanction.duration_days, applied_at=applied_at)

@app.get("/api/sanctions/user/{user_id}", response_model=List[SanctionResponse], tags=["Sanctions"])
def get_user_sanctions(user_id: int):
    """Lister les sanctions d'un utilisateur."""
    conn = sqlite3.connect("moderation.db")
    cursor = conn.cursor()
    cursor.execute("SELECT id, user_id, sanction_type, reason, duration_days, applied_at FROM sanctions WHERE user_id = ?", (user_id,))
    rows = cursor.fetchall()
    conn.close()
    return [SanctionResponse(id=r[0], user_id=r[1], sanction_type=r[2],
        reason=r[3], duration_days=r[4], applied_at=r[5]) for r in rows]

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=PORT, reload=True)
