import json
import requests
from requests import Response
from typing import List, Dict

from sets.CustomerReview import CustomerReview

KEY: str = "6a486812184644159381c49b03c20e5c"
KEY_HEADER: str = "Ocp-Apim-Subscription-Key"
AUTH_HEADERS: Dict[str, str] = {KEY_HEADER: KEY}

ENDPOINT: str = "https://westeurope.api.cognitive.microsoft.com/text/analytics/v2.0/sentiment"


class SentimentReqDocument:
    def __init__(self, language: str, docid: int, text: str) -> None:
        super().__init__()
        self.language = language
        self.docid = docid
        self.text = text


class SentmentReqResponse:
    def __init__(self, score: float, docid: int) -> None:
        self.score = score
        self.docid = docid


def build_req_for_reviews(language: str, reviews: List[CustomerReview]) -> List[SentimentReqDocument]:
    docs: List[SentimentReqDocument] = []
    for index in range(len(reviews)):
        docs.append(SentimentReqDocument(language, index, reviews[index].review_body))
    return docs


def build_request_body(req_docs: List[SentimentReqDocument]) -> str:
    req_docs_json: List[str] = list(map(lambda elem: json.dumps(elem.__dict__), req_docs))
    return "{\"documents\": " + str(req_docs_json).replace("\'{", "{").replace("\'}", "}").replace("docid", "id") + "}"


def send_request(body: str) -> Response:
    return requests.post(ENDPOINT, headers=AUTH_HEADERS, json=body)


def analyze_reviews(reviews: List[CustomerReview]):
    reqdocs = build_req_for_reviews(reviews[0].marketplace.lower(), reviews)
    reqdocs_body = build_request_body(reqdocs)
    resp = send_request(reqdocs_body)

    print(str(resp))
