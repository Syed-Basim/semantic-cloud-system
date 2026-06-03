from fastapi import FastAPI

from app.routes.document_routes import router as document_router
from app.routes.search_routes import router as search_router
from app.routes.index_routes import router as index_router

app = FastAPI()

app.include_router(document_router)
app.include_router(search_router)
app.include_router(index_router)