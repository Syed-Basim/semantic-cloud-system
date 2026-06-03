import pandas as pd


def extract_excel_text(file_path: str) -> str:

    excel_file = pd.ExcelFile(file_path)

    combined_text = []

    for sheet_name in excel_file.sheet_names:

        df = pd.read_excel(file_path, sheet_name=sheet_name)

        combined_text.append(
            f"Sheet Name: {sheet_name}\n"
        )

        combined_text.append(
            df.to_string(index=False)
        )

    return "\n".join(combined_text)