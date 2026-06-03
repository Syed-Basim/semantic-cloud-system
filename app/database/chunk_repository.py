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
            %s
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