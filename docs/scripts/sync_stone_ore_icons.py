"""Copy verified flat block textures for the stone and ore wiki pages."""

import json
import shutil
from pathlib import Path


ROOT = Path(__file__).resolve().parents[2]
MODELS = ROOT / "src/main/resources/assets/byg/models/block"
TEXTURES = ROOT / "src/main/resources/assets/byg/textures"
OUTPUT = ROOT / "docs/src/assets/blocks"
BLOCKS = (
    "sepinite", "soapstone", "sodalite", "scoria",
    "light_blue_crystal_block", "purple_crystal_block",
    "red_crystal_block", "white_crystal_block",
    "tamrelite_ore", "pendorite_ore", "latharium_ore", "kasai_ore",
)


def main() -> None:
    OUTPUT.mkdir(parents=True, exist_ok=True)
    for block_id in BLOCKS:
        model = json.loads((MODELS / f"{block_id}.json").read_text(encoding="utf-8"))
        textures = model["textures"]
        if model["parent"] == "block/cube_all":
            texture = textures["all"]
        else:
            assert model["parent"] == "block/cube", block_id
            faces = ("down", "up", "north", "east", "south", "west")
            assert len({textures[face] for face in faces}) == 1, block_id
            texture = textures["north"]
        assert texture.startswith("byg:"), (block_id, texture)
        source = TEXTURES / f"{texture.removeprefix('byg:')}.png"
        assert source.is_file(), (block_id, source)
        shutil.copyfile(source, OUTPUT / f"{block_id}.png")
        print(f"{block_id} -> {source.name}")


if __name__ == "__main__":
    main()
