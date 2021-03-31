# Mini-game server

A fully working, on-demand, hosts-compatible Minecraft Mini-game server (may contains tons of bugs), created back in 2020.

Few important features:
- On-demand servers
- Creation of Hosts servers for any mini-game with tons of configuration, like the UHC-Hosts in Erisium
- Automatic instances management (creation, deletion, hub balancing)
- Complete APIs to easily create mini-games
- Maps management

Uses **MongoDB** as the database, **Redis** as the cache and messages broker, and **Docker Swarm** as the cluster manager. You will need a docker swarm registry to publish and use the games images. The mini-games specific files (maps, spigot configuration) are downloaded from a **S3** bucket, based on the map.

To communicate and get information about each server instance, I've created a system called Feeders, which publish messages and data to Redis. You can then listen to them from any other server instance.

## Modules:

### Apollon
The API used for the Bungecord plugins. Contains the account manager, base commands, mongo and redis managers, maintenance system, feeders system and friends system.

### Zeus
It's the on-demand Bungeecord plugin which operates the server instances. Contains the rank, hub and instances managing commands, hub balancing and server instances system.

### API
The API used for all the spigot plugins. Contains the account manager, base commands, mongo and redis managers, feeders system, gui system, defaults listeners, npc system and scoreboard system.

### Lobby
The plugin for the lobby. Contains pet system, hub system, joining games, creating / joining Hosts servers and more.

### Poseidon
It's the mini-games API which simplify a lot the process of creating new mini-games. It includes a system to create Hosts by modifying the default game configuration. Contains basic commands, configuration system, game management, scoreboards system and more.

### Mini-games
I've implemented few mini-games to test the mini-games API:

- Duels: A 1v1 duels, with podium, spectating and kits.
- Shooter: A Quakecraft game
- Capture the Flag: A capture the flags, with kits and teams
