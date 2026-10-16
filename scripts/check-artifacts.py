#!/usr/bin/env python3
"""Verify licensed media provenance and the playable APK package."""

import hashlib
import json
from pathlib import Path
import xml.etree.ElementTree as ET
import zipfile

ROOT = Path(__file__).resolve().parent.parent

for path in list((ROOT / "res").rglob("*.xml")) + list((ROOT / "art").glob("*.svg")):
    ET.parse(path)

media = json.loads((ROOT / "assets/media.json").read_text())
recording = ROOT / media["file"]
assert recording.stat().st_size == media["bytes"], "Recording size changed"
assert hashlib.sha256(recording.read_bytes()).hexdigest() == media["sha256"], "Recording hash changed"
for item in json.loads((ROOT / "assets/materials/provenance.json").read_text()):
    material = ROOT / "assets/materials" / item["file"]
    assert hashlib.sha256(material.read_bytes()).hexdigest() == item["sha256"], material

with zipfile.ZipFile(ROOT / "build/twilight-hearth.apk") as apk:
    for name in ("NOTICE", "LICENSE"):
        assert apk.read("assets/" + name) == (ROOT / name).read_bytes(), name
    # MediaPlayer opens this resource through a file descriptor.
    assert apk.getinfo(media["file"]).compress_type == zipfile.ZIP_STORED
    assert hashlib.sha256(apk.read(media["file"])).hexdigest() == media["sha256"]
print("Licensed media, material hashes, XML and packaged offline credits are valid.")
