@echo off
openssl genrsa -out "%1.key" 4096
openssl rsa -in "%1.key" -pubout -out "%1.pub"