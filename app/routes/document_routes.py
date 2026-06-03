from fastapi import APIRouter
from fastapi import UploadFile
from fastapi import File

import os
import tempfile

from app.extraction.pdf_extractor import extract_pdf_text
from app.extraction.docx_extractor import extract_docx_text
from app.extraction.txt_extractor import extract_txt_text
from app.extraction.excel_extractor import extract_excel_text

from app.chunking.text_chunker import chunk_text

router = APIRouter()


@router.post("/extract")
async def extract_document(
        file: UploadFile = File(...)
):
    suffix = os.path.splitext(
        file.filename
    )[1].lower()

    with tempfile.NamedTemporaryFile(
            delete=False,
            suffix=suffix
    ) as temp_file:

        content = await file.read()

        temp_file.write(content)

        temp_path = temp_file.name

    try:

        # Extract text based on file type
        if suffix == ".pdf":
            text = extract_pdf_text(temp_path)

        elif suffix == ".docx":
            text = extract_docx_text(temp_path)

        elif suffix == ".txt":
            text = extract_txt_text(temp_path)

        elif suffix in [".xlsx", ".xls"]:
            text = extract_excel_text(temp_path)

        else:
            return {
                "error": f"Unsupported file type: {suffix}"
            }

        # Generate chunks
        chunks = chunk_text(text)

        return {
            "filename": file.filename,
            "extracted_text": text,
            "file_type": suffix,
            "total_characters": len(text),
            "total_chunks": len(chunks),
            "first_chunk": chunks[0],
            "last_chunk": chunks[-1]
        }

    finally:
        if os.path.exists(temp_path):
            os.remove(temp_path)