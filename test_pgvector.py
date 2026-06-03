from app.embeddings.embedding_service import generate_embedding
from app.database.chunk_repository import save_chunk

text = """
Face Recognition based Smart Door Lock
"""

embedding = generate_embedding(text)

save_chunk(
    file_id=1,
    chunk_index=0,
    chunk_text=text,
    embedding=embedding
)

print("Chunk stored successfully.")
