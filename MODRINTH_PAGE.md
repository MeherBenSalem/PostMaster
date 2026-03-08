# PostMaster

**PostMaster** is a lightweight and powerful mailbox plugin for Paper/Folia servers, now packaged under `io.nightbeam.postmaster`.

## Features

- 📬 **Offline & Online Mail**: Send mail to players whether they're online or offline.
- 📦 **Custom Vouchers**: Define vouchers redeemable through the mail system.
- 🛠️ **Configurable Storage**: Support for YAML, SQLite, MySQL, and HikariCP-based connections.
- 🎨 **Intuitive GUI**: Built-in inventory-based interfaces for viewing mail, composing messages, and managing vouchers.
- 🧠 **API & Bridge**: Expose an API for other plugins and allow easy interoperability.
- 🏷️ **Permissions Support**: Fine-grained command permissions (`postmaster.use`, etc.).
- ⚙️ **Scheduler Integration**: Background tasks handle mail expiration and notification delivery.
- ✅ **Folia-friendly**: Fully compatible with Paper and Folia builds.

## Installation

1. Place the JAR in your server's `plugins/` folder.
2. Restart or reload the server.
3. Configure `config.yml` to choose your preferred storage and customize messages.

## Configuration

The plugin ships with a sample `config.yml` which allows you to adjust storage settings, voucher definitions, and GUI colors. Example voucher files can be found under `vouchers/example-voucher.yml`.

## Why PostMaster?

PostMaster offers a smooth in-game experience with minimal configuration. Its modular architecture makes it easy to extend or integrate with other plugins. Whether you're running a small survival server or a bustling network, PostMaster keeps your players connected with reliable mail delivery.

---
*For more information, support, or to purchase a licence, visit the official listing on Modrinth.*
