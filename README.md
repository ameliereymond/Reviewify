# `Reviewify`

### Development requirements
- Python-side (data analysis and visualization):
  - [**Python 3.5+**](https://www.python.org/)
  - [**pipenv**](https://pipenv.readthedocs.io/en/latest/) : Python dependencies manager (matplotlib, numpy...)
  - [**Git LFS**](https://git-lfs.github.com/) : Large file storage on Github (for datasets)
  - **Python IDE** recommended : [PyCharm](https://www.jetbrains.com/pycharm/) has good integration with `pipenv`
  
  Then run `pipenv update` in this folder to download all required dependencies and then run the main entrypoint ([`reviewify.py`](./reviewify.py))

- [Java-side](./azure-sent-analysis) (Uses Microsoft Azure API to get a sentiment score for each review):
  - A Java JDK 11+ such as [**OpenJDK**](https://adoptopenjdk.net/?variant=openjdk11&jvmVariant=hotspot) (no installer) or [**Oracle JDK**](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html) (installer)
  - [**Maven 3.5+**](https://maven.apache.org/) : Java dependency manager and build system
  - [**Microsoft Sentiment Analysis API Key**](https://docs.microsoft.com/en-us/azure/cognitive-services/text-analytics/how-tos/text-analytics-how-to-sentiment-analysis) : Need a developer Azure account and use of LUIS sentiment analysis service
  - **Java IDE** recommended : [IntelliJ IDEA](https://www.jetbrains.com/idea/) is nice and integrates well with the whole project

  Then run, into [`azure-sent-analysis`](./azure-sent-analysis) folder, the command `mvn clean spring-boot:run` to execute the Java project.
  
