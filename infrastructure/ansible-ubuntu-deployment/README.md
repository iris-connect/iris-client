# IRIS Client - Ansible Playbook

Playbook for installing one or multiple IRIS Client instances. This installation assumes that all clients run under the same root (sub)domain (e.g. client-1.domain.de, client-2.domain.de) and thus use the same wildcard ssl certificate ( e.g. *.domain.de ).

This guide assumes that you are already aware of the [IRIS Client Docker Compose Installation](https://github.com/iris-connect/iris-client/blob/develop/infrastructure/deployment/docs/Installation-Docker-Compose.md) and that you have your certificates and keys in handy.

## Compatability

Tested with Ubuntu LTS 18.04.

## Installation Ansible

Running the playbook requires the latest version of Ansible.

1. Remove any old version.

    ```
    sudo apt remove ansible && sudo apt --purge autoremove
    ```

1. Add Ansible repos.

    ```
    sudo apt install software-properties-common
    sudo apt-add-repository ppa:ansible/ansible
    ```

1. Update your repos. 

    ```
    sudo apt update
    ```

1. Install Ansible.

    ```
    sudo apt install ansible
    ```

1. Check Ansible version.

    ```
    ansible --version
    # ansible 2.9.24
    ```


## Project structure

You need to download the whole folder structure. It contains files and folders needed by the playbook. It also gives you a structure how to customize your personal installation.

```
.
├── certs
│   ├── eps             # This is where your EPS and PROXY certificates and keys go.
│   └── nginx           # This is where your wildcard certificate and key goes.
├── includes            # Modules needed by the playbook.
├── scripts             # Scripts needed by the playbook.
|── templates           # JNinja Templates needed by the playbook.
├── playbook.yaml       # The actual Ansible playbook.
└── vars.yaml.example   # An example of the vars file.
```

## Installing IRIS Clients with Ansible.

1. Copy your SSL wildcard certificate and key to `certs/nginx`.
1. Copy your EPS and Proxy certificates and keys to `certs/eps`.
1. Rename file `vars.yaml.example`.
    ```
    mv vars.yaml.example vars.yaml
    ```
1. Edit `vars.yaml` and add your customized iris-client configurations.
1. Run playbook
    ```
    ansible-playbook playbook.yaml
    ```
    This will install all clients to `<Your Project Dir>/iris-clients`. You can override the installation dir.
    ```
    ansible-playbook playbook.yaml --extra-vars "install_dir=/opt/your-preferred-dir" 
    ```
    After the playbook is finished all clients are registered with systemd and already started for you.

## Useful commands

1. Start and stop a specific iris-client instance.
    ```
    systemctl start <iris-client-name>
    systemctl stop <iris-client-name>
    ```
1. See status of a specific iris-client instance.
    ```
    systemctl status <iris-client-name>
    ```
1. See logs of a specific iris-client instance.
    ```
    journalctl -fu <iris-client-name>
    ```



