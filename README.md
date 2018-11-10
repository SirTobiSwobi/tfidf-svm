TFIDF feature extractor with libsvm classifier implementing the classifier trainer API of the athlete/trainer pattern. 

See tfidfsvm.yml config file for metadata about the microservice. The same metadata can be accesses by calling /metadata of the running service. 

The Dockerfile to see which commands you need to run the service on a Linux machine with Java. 

Or you can just run the Docker container including everything necessary. 

Version change log:

- 0.0.1: running clone of tfidf v0.1.5 implementing classifier-trainer API
- 0.0.2: set up dependencies for libsvm
- 0.0.3: implemnted libsvm using its default configuration with tfidf feature vectors read from configuration
- 0.0.4: implemented svm training. RBF gives poor results, linear seperation is an order of magnitude better when using tfidf feature vectors. 
