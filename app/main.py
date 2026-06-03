from fastapi import FastAPI
from app.routes.document_routes import router

app = FastAPI()

app.include_router(router)