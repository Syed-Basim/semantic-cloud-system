from app.database.db import get_connection


def save_chunk(
        file_id: int,
        chunk_index: int,
        chunk_text: str,
        embedding: list
):
    conn = get_connection()

    cursor = conn.cursor()

    vector_string = "[" + ",".join(
        str(x) for x in embedding
    ) + "]"

    cursor.execute(
        """
        INSERT INTO document_chunks
        (
            file_id,
            chunk_index,
            chunk_text,
            embedding
        )
        VALUES
        (
            %s,
            %s,
            %s,
            %s::vector
        )
        """,
        (
            file_id,
            chunk_index,
            chunk_text,
            vector_string
        )
    )

    conn.commit()

    cursor.close()
    conn.close()

def search_chunks(
        query_embedding: list,
        top_k: int = 5
):
    conn = get_connection()

    cursor = conn.cursor()

    vector_string = "[" + ",".join(
        str(x) for x in query_embedding
    ) + "]"

    cursor.execute(
        """
        SELECT
            file_id,
            chunk_index,
            chunk_text,
            1 - (embedding <=> %s::vector)
                AS similarity
        FROM document_chunks
        ORDER BY embedding <=> %s::vector
        LIMIT %s
        """,
        (
            vector_string,
            vector_string,
            top_k
        )
    )

    results = cursor.fetchall()

    cursor.close()
    conn.close()

    return results