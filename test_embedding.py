from app.embeddings.embedding_service import generate_embedding

sample_text = """
Face Recognition based Smart Door Lock
"""

embedding = generate_embedding(
    sample_text
)

print("Embedding Length:", len(embedding))
print("First 10 Values:")
print(embedding[:10])