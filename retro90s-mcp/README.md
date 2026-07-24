# Retro 90s MCP Server (`retro90s-mcp`)

A Model Context Protocol (MCP) server providing a nostalgic and historically accurate knowledge base, personality prompt, and specialized search tools covering the 1990s decade (1990–1999).

---

## 🚀 Overview

`retro90s-mcp` connects LLMs to structured historical data and a themed persona from the 1990s. It contains curated datasets across 15 pop-culture, computing, and historical categories, combined with a 90s tech expert persona prompt.

---

## 📁 Knowledge Categories

The server includes 15 distinct JSON knowledge bases located in `src/main/resources/knowledge/`:

| Category | File | Description |
|---|---|---|
| **Technology** | `technology.json` | Handheld PDAs, Zip drives, portable CD audio, DVD formats, and translucent iMacs. |
| **Internet** | `internet.json` | Web browsers, AOL keywords, GeoCities, AltaVista, ICQ instant messaging. |
| **Windows** | `windows.json` | Windows 3.1, Windows 95, Windows 98, Windows NT 4.0, and Internet Explorer. |
| **Linux** | `linux.json` | Early Linux kernel releases, Slackware, Debian, Red Hat, and Tux the Penguin. |
| **DOS** | `dos.json` | MS-DOS 6.22, Norton Commander, QBasic, DOS extenders, and conventional RAM management. |
| **Games** | `games.json` | Iconic FPS, RTS, and 3D action titles like DOOM, Quake, Half-Life, StarCraft, and Zelda OOT. |
| **Consoles** | `consoles.json` | 16-bit to 128-bit hardware including SNES, PlayStation 1, Nintendo 64, Saturn, and Dreamcast. |
| **Programming** | `programming.json` | Emergence of Java, JavaScript, Python 1.0, Delphi, and server-side PHP. |
| **Hardware** | `hardware.json` | 3dfx Voodoo graphics cards, Intel Pentium CPUs, Sound Blaster 16, and alternative CPUs. |
| **Movies** | `movies.json` | 90s cinematic milestones like Jurassic Park, The Matrix, Pulp Fiction, Titanic, and Toy Story. |
| **Music** | `music.json` | Grunge rock, alternative, big beat rave, East Coast hip-hop, and 90s pop phenomena. |
| **Television** | `television.json` | Definitive TV series including The X-Files, Friends, Seinfeld, Twin Peaks, and Pokémon. |
| **Fashion** | `fashion.json` | Grunge flannels, JNCO wide-leg jeans, neon windbreakers, platforms, and snapbacks. |
| **History** | `history.json` | Fall of the Soviet Union, German reunification, Hubble Telescope, Y2K panic, and Dolly the Sheep. |
| **Brands** | `brands.json` | 90s pop culture brands including Tamagotchi, Beanie Babies, Blockbuster Video, Nike Air, and Game Boy. |

---

## 🏗️ Architecture & Schema

Each entry across all knowledge files conforms to a strict JSON schema:

```json
[
  {
    "id": "unique-slug-year",
    "title": "Title of Item",
    "category": "category-name",
    "year": 1995,
    "manufacturer": "Company or Creator",
    "summary": "Concise summary of significance.",
    "facts": [
      "Historically accurate technical or cultural fact 1.",
      "Historically accurate technical or cultural fact 2."
    ],
    "related": [
      "related-item-id-1",
      "related-item-id-2"
    ],
    "keywords": [
      "search-term-1",
      "search-term-2"
    ]
  }
]
```

### Prompt Resource
- **Personality Prompt**: Located at `src/main/resources/prompts/personality.md`, defining the **Cyber-Steve** 90s guru persona.

---

## 🛠️ Build & Verification

Requirements:
- Java 21+
- Maven 3.8+

### Compile Project
```bash
mvn clean compile -f retro90s-mcp/pom.xml
```

---

## 📜 License

MIT License. Built as part of the Java MCP Testing Demo suite.
