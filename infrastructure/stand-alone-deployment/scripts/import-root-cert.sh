#!/bin/bash

source .env

# Help
if [ -n "$1" -a "$1" != "force" ]; then
    echo "Usage:
        Argument 'force': Forces the import of the certificate and overrides the existing one.
        Environment variable 'STOREPASS': The storepass of the Java key store if you use an other then the default 'changeit'.
        
        This script uses 'sudo' to change system key stores. Please make sure that sudo is present and that you have the necessary permissions."
        
    exit 0
fi

# Determines CA folder to use
pw="${STOREPASS:-changeit}"
alias_prefix=iris_

if [ "$IRIS_ENV" == "live" ]; then 
    ca_folder=ca/live/*
elif [ "$IRIS_ENV" == "staging" ]; then 
    ca_folder=ca/staging/*
elif [ -z "$IRIS_ENV" -a -z "$crt_file" -a -z "$ca_folder" ]; then
    echo "The environment variable 'IRIS_ENV' musst be declareted in file '.env'! (Or declare 'ca_folder' or 'crt_file' for development.)"
    exit 1
elif [ -z "$crt_file" ]; then
    ca_folder=$crt_file
fi

# Checks if all root certificates in the ca_folder are imported
if [ "$1" != "force" ]; then

    output=`keytool -list -cacerts -storepass $pw` 
    
    if (( $? )); then
        
        echo "Error: $output"
        exit 1
    fi
    
    import=0
    
    for crt in $ca_folder; do
    
        alias=$alias_prefix${crt##*/}
        echo $output | grep -qi $alias
        
        if (( $? )); then
            import=1
        fi
    done
    
    if (( ! $import )); then
        
        echo "The root certificates already exists. Use the argument 'force' to override this existing certificates."
        exit 0
    fi
fi

# Deletes old certificates and imports all certificates from ca_folder. This would also allow the replacement of certificates with newer ones.
for crt in $ca_folder; do
    
    alias=$alias_prefix${crt##*/}

    echo "Deletes old root certificate with alias $alias from key store."
    sudo keytool -delete -alias $alias -cacerts -storepass $pw
    
    echo "Imports root certificate from file $crt with alias $alias."
    sudo keytool -importcert -alias $alias -cacerts -storepass $pw -noprompt -file $crt
done
