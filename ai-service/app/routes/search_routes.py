from fastapi import APIRouter
from pydantic import BaseModel

from app.embeddings.embedding_service import (
    generate_embedding
)

from app.database.chunk_repository import (
    search_chunks
)

router = APIRouter()


class SearchRequest(BaseModel):
    query: str
    top_k: int = 5


@router.post("/search")
def semantic_search(
        request: SearchRequest
):
    embedding = generate_embedding(
        request.query
    )

    results = search_chunks(
        embedding,
        request.top_k
    )

    response = []

    for row in results:
        response.append(
            {
                "file_id": row[0],
                "chunk_index": row[1],
                "chunk_text": row[2],
                "similarity": float(row[3])
            }
        )

    return {
        "query": request.query,
        "results": response
    }