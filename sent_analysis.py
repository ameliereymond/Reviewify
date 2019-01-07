from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer

analyser = SentimentIntensityAnalyzer()


def sentiment_analyzer_scores(sentence) -> float:
    score = analyser.polarity_scores(sentence)
    return score['compound']

