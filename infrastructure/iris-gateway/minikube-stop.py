#!/usr/bin/env python3

import os

here = os.path.dirname(os.path.realpath(__file__))
os.chdir(here)

os.system('./minikube-check-dependencies.py')
os.system('minikube stop')
