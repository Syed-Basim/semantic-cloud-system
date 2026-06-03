import psycopg2


def get_connection():
    return psycopg2.connect(
        host="localhost",
        port="5432",
        database="semantic_cloud_db",
        user="postgres",
        password="root"
    )