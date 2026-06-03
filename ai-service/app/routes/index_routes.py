from fastapi import APIRouter
import os

from app.models.index_request import IndexRequest

from app.extraction.pdf_extractor import extract_pdf_text
from app.extraction.docx_extractor import extract_docx_text
from app.extraction.txt_extractor import extract_txt_text
from app.extraction.excel_extractor import extract_excel_text

from app.chunking.text_chunker import chunk_text

from app.embeddings.embedding_service import generate_embedding

from app.database.chunk_repository import save_chunk

router = APIRouter()


@router.post("/index-document")
def index_document(request: IndexRequest):

    if not os.path.exists(request.file_path):
        return {
            "error": f"File not found: {request.file_path}"
        }

    suffix = os.path.splitext(
        request.file_path
    )[1].lower()

    # Extract text based on file type
    if suffix == ".pdf":

        text = extract_pdf_text(
            request.file_path
        )

    elif suffix == ".docx":

        text = extract_docx_text(
            request.file_path
        )

    elif suffix == ".txt":

        text = extract_txt_text(
            request.file_path
        )

    elif suffix in [".xlsx", ".xls"]:

        text = extract_excel_text(
            request.file_path
        )

    else:

        return {
            "error":
                f"Unsupported file type: {suffix}"
        }

    # Chunk text
    chunks = chunk_text(text)

    # Store chunks and embeddings
    for index, chunk in enumerate(chunks):

        print(f"Processing chunk {index}")

        embedding = generate_embedding(chunk)

        print(f"Generated embedding for chunk {index}")

        save_chunk(
            file_id=request.file_id,
            chunk_index=index,
            chunk_text=chunk,
            embedding=embedding
        )

        print(f"Saved chunk {index}")

    return {
        "file_id": request.file_id,
        "file_name": os.path.basename(
            request.file_path
        ),
        "total_characters": len(text),
        "total_chunks": len(chunks),
        "message": "Document indexed successfully"
    }