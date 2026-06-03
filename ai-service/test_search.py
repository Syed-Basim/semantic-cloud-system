from app.embeddings.embedding_service import (
    generate_embedding
)

from app.database.chunk_repository import (
    search_chunks
)

query = "machine learning"

embedding = generate_embedding(query)

results = search_chunks(
    embedding,
    top_k=5
)

for row in results:
    print(row)