import os
from PyPDF2 import PdfReader
import logging
from pathlib import Path
import unicodedata
import re

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler('pdf_converter.log'),
        logging.StreamHandler()
    ]
)


def normalize_text(text):
    """
    Normaliza o texto, preservando caracteres especiais e estrutura.
    """
    try:

        text = ''.join(char if unicodedata.category(char)[
                       0] != "C" or char in '\n\t' else ' ' for char in text)
        text = re.sub(r'\s+', ' ', text)
        text = re.sub(r'\n\s*\n', '\n\n', text)

        return text.strip()
    except Exception as e:
        logging.error(f"Erro na normalização do texto: {str(e)}")
        return text


def extract_text_from_pdf(pdf_path):
    """
    Extrai texto de um PDF com tratamento de erros e preservação de estrutura.
    """
    try:
        reader = PdfReader(pdf_path)
        text_content = []

        for page_num, page in enumerate(reader.pages, 1):
            try:
                page_text = page.extract_text()
                page_text = normalize_text(page_text)
                text_content.append(f"\n[Página {page_num}]\n{page_text}")

            except Exception as e:
                logging.error(f"Erro ao processar página {
                              page_num} do arquivo {pdf_path}: {str(e)}")
                text_content.append(f"\n[Erro na Página {page_num}]\n")
                continue

        return '\n'.join(text_content)

    except Exception as e:
        logging.error(f"Erro ao processar o arquivo {pdf_path}: {str(e)}")
        return f"ERRO NA CONVERSÃO: {str(e)}"


def convert_pdfs_to_txt(input_folder, output_folder):
    """
    Converte todos os PDFs de uma pasta para arquivos TXT.
    """

    Path(output_folder).mkdir(parents=True, exist_ok=True)
    pdf_files = [f for f in os.listdir(
        input_folder) if f.lower().endswith('.pdf')]

    if not pdf_files:
        logging.warning(f"Nenhum arquivo PDF encontrado em {input_folder}")
        return

    for pdf_file in pdf_files:
        try:
            pdf_path = os.path.join(input_folder, pdf_file)
            txt_filename = os.path.splitext(pdf_file)[0] + '.txt'
            txt_path = os.path.join(output_folder, txt_filename)

            logging.info(f"Convertendo: {pdf_file}")
            text_content = extract_text_from_pdf(pdf_path)
            with open(txt_path, 'w', encoding='utf-8') as txt_file:
                txt_file.write(text_content)

            logging.info(f"Arquivo convertido com sucesso: {txt_filename}")

        except Exception as e:
            logging.error(f"Erro ao converter {pdf_file}: {str(e)}")
            continue


if __name__ == "__main__":
    input_folder = "pdfs"
    output_folder = "txts"

    logging.info("Iniciando conversão de PDFs para TXT...")
    convert_pdfs_to_txt(input_folder, output_folder)
    logging.info("Processo de conversão finalizado!")
