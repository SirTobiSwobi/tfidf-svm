# TFIDF-SVM
This repository contains a contribution of the PhD Thesis *Bootstrapping Explainable Text Categorization in Emergent Knowledge-Domains* by Tobias Eljasik-Swoboda submitted to the University of Hagen, Germany on November 30th, 2020. Date of oral examiniation: March 2nd, 2021. 

The TFIDF-SVM microservice combines a TFIDF based feature extractor with the libsvm classifier implementing the classifier trainer API of the athlete/trainer pattern. 

See tfidfsvm.yml config file for metadata about the microservice. The same metadata can be accesses by calling /metadata of the running service. 

The Dockerfile to see which commands you need to run the service on a Linux machine with Java. These are the same on BaSH and essentially also on Powershell.

Or you can just run the Docker container including everything necessary. 

Version change log:

- 0.0.1: running clone of tfidf v0.1.5 implementing classifier-trainer API
- 0.0.2: set up dependencies for libsvm
- 0.0.3: implemnted libsvm using its default configuration with tfidf feature vectors read from configuration
- 0.0.4: implemented svm training. RBF gives poor results, linear seperation is an order of magnitude better when using tfidf feature vectors. 
- 0.0.5: implemented modular evaluation set generation and automated weighting. Up to .79 F1 during n-fold cross-validation. linear still better than RBF
- 0.1.0: updated /model and /models endpoints to accomodate the libsvm model
- 0.1.1: updated web GUI
- 1.0.0: finished /classification endpoint and fixed /model endpoint
- 1.0.1: fixed configuration GUI
- 1.0.2: fixed bug in /configurations/x
- 1.1.0: Implementing explainability as extension for every categorization.
- 1.1.1: Implemented missed functionality of probability estimation. 
- 1.1.2: Fixed Java Script API Library Issue. 