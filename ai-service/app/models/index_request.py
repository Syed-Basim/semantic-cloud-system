from pydantic import BaseModel


class IndexRequest(BaseModel):
    file_id: int
    file_path: str