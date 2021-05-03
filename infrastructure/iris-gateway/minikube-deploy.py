#!/usr/bin/env python3

import os

here = os.path.dirname(os.path.realpath(__file__))
os.chdir(here)

os.system('./minikube-check-dependencies.py')
os.system('./minikube-start.py')

os.system('helm upgrade --install --namespace iris-gateway --create-namespace --set environment=local iris-gateway .')
