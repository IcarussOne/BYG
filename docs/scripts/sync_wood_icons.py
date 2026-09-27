"""Create wood icons from the textures named by block and item models."""

import json
import shutil
from pathlib import Path
from PIL import Image


ROOT = Path(__file__).resolve().parents[2]
MODELS = ROOT / "src/main/resources/assets/byg/models/block"
TEXTURES = ROOT / "src/main/resources/assets/byg/textures"
OUTPUT = ROOT / "docs/src/assets/wood"

FAMILY_MODELS = {
    "Aspen": "aspen_planks",
    "Baobab": "baobab_planks",
    "Cherry": "cherry_planks",
    "Cika": "cika_planks",
    "Cypress": "cypress_planks",
    "Ebony": "ebony_planks",
    "Enchanted (blue)": "enchanted_planks",
    "Enchanted (green)": "green_enchanted_planks",
    "Fir": "fir_planks",
    "Frozen Oak": "frozen_oak_planks",
    "Great Oak": "great_oak_planks",
    "Hawthorn": "hawthorn_planks",
    "Holly": "holly_planks",
    "Ironwood": "ironwood_planks",
    "Jacaranda": "jacaranda_planks",
    "Mahogany": "mahogany_planks",
    "Mangrove": "mangrove_planks",
    "Maple": "maple_planks",
    "Palm": "palm_planks",
    "Pine": "pine_planks",
    "Rainbow Eucalyptus": "rainbow_eucalyptus_planks",
    "Redwood": "redwood_planks",
    "Rowan": "rowan_planks",
    "Skyris": "skyris_planks",
    "Willow": "willow_planks",
    "Witch Hazel": "witch_hazel_planks",
    "Zelkova": "zelkova_planks",
}

EXTRA_MODELS = {"Enchanted (blue)": "enchanted", "Enchanted (green)": "green_enchanted"}


def texture_path(name: str) -> Path:
    assert name.startswith("byg:"), name
    source = TEXTURES / f"{name.removeprefix('byg:')}.png"
    assert source.is_file(), source
    return source


def draw_face(canvas: Image.Image, source: Image.Image, corners: tuple, shade: float) -> None:
    # Affine map each texture pixel into two triangles of an isometric face.
    from PIL import ImageDraw

    draw = ImageDraw.Draw(canvas)
    a, b, c, d = corners
    width, height = source.size
    for y in range(height):
        for x in range(width):
            color = source.getpixel((x, y))
            if len(color) == 3:
                color = (*color, 255)
            if color[3] == 0:
                continue
            color = tuple(round(channel * shade) for channel in color[:3]) + (color[3],)
            def point(u: float, v: float) -> tuple[float, float]:
                return (
                    a[0] * (1-u) * (1-v) + b[0] * u * (1-v) + c[0] * u * v + d[0] * (1-u) * v,
                    a[1] * (1-u) * (1-v) + b[1] * u * (1-v) + c[1] * u * v + d[1] * (1-u) * v,
                )
            points = [point(x / width, y / height), point((x+1) / width, y / height),
                      point((x+1) / width, (y+1) / height), point(x / width, (y+1) / height)]
            draw.polygon(points, fill=color)


def render_cube(block_id: str) -> None:
    model = json.loads((MODELS / f"{block_id}.json").read_text(encoding="utf-8"))
    textures = model["textures"]
    assert model["parent"] in ("block/cube", "block/cube_all"), block_id
    def face(name: str) -> Image.Image:
        key = textures["all"] if "all" in textures else textures[name]
        return Image.open(texture_path(key)).convert("RGBA")
    canvas = Image.new("RGBA", (32, 32))
    draw_face(canvas, face("north"), ((1, 8), (16, 16), (16, 31), (1, 23)), 0.78)
    draw_face(canvas, face("east"), ((16, 16), (31, 8), (31, 23), (16, 31)), 0.62)
    draw_face(canvas, face("up"), ((16, 0), (31, 8), (16, 16), (1, 8)), 1.0)
    canvas.save(OUTPUT / f"{block_id}.png")


def main() -> None:
    OUTPUT.mkdir(parents=True, exist_ok=True)
    for family, block_id in FAMILY_MODELS.items():
        model = json.loads((MODELS / f"{block_id}.json").read_text(encoding="utf-8"))
        textures = model["textures"]
        if model["parent"] == "block/cube_all":
            texture = textures["all"]
        else:
            assert model["parent"] == "block/cube", (family, model["parent"])
            faces = ("down", "up", "north", "east", "south", "west")
            assert len({textures[face] for face in faces}) == 1, (family, textures)
            texture = textures["north"]
        source = texture_path(texture)
        shutil.copyfile(source, OUTPUT / f"{block_id}.png")
        print(f"{family}: {block_id} -> {source.name}")
        stem = EXTRA_MODELS.get(family, block_id.removesuffix("_planks"))
        for suffix in ("log", "wood", "stripped_log"):
            cube_stem = "blue_enchanted" if family == "Enchanted (blue)" and suffix != "log" else stem
            cube_id = f"stripped_{cube_stem}_log" if suffix == "stripped_log" else f"{cube_stem}_{suffix}"
            if (MODELS / f"{cube_id}.json").is_file():
                render_cube(cube_id)
        door_stem = "blue_enchanted" if family == "Enchanted (blue)" else stem
        door_id = f"{door_stem}_door"
        item_model = ROOT / f"src/main/resources/assets/byg/models/item/{door_id}.json"
        if item_model.is_file():
            item = json.loads(item_model.read_text(encoding="utf-8"))
            if item["parent"] == "item/generated":
                shutil.copyfile(texture_path(item["textures"]["layer0"]), OUTPUT / f"{door_id}.png")
    for cube_id in ("palo_verde_log", "palo_verde_wood", "stripped_palo_verde_log"):
        if (MODELS / f"{cube_id}.json").is_file():
            render_cube(cube_id)


if __name__ == "__main__":
    main()
