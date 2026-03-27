from pathlib import Path
import struct
import zlib


OUT = Path(__file__).resolve().parents[1] / "docs" / "images" / "preview.png"
OUT.parent.mkdir(parents=True, exist_ok=True)

WIDTH = 1800
HEIGHT = 1120
BACKGROUND = (246, 244, 239)

FONT = {
    "A": ["01110", "10001", "10001", "11111", "10001", "10001", "10001"],
    "B": ["11110", "10001", "10001", "11110", "10001", "10001", "11110"],
    "C": ["01111", "10000", "10000", "10000", "10000", "10000", "01111"],
    "D": ["11110", "10001", "10001", "10001", "10001", "10001", "11110"],
    "E": ["11111", "10000", "10000", "11110", "10000", "10000", "11111"],
    "G": ["01111", "10000", "10000", "10111", "10001", "10001", "01111"],
    "H": ["10001", "10001", "10001", "11111", "10001", "10001", "10001"],
    "I": ["11111", "00100", "00100", "00100", "00100", "00100", "11111"],
    "J": ["00111", "00010", "00010", "00010", "00010", "10010", "01100"],
    "K": ["10001", "10010", "10100", "11000", "10100", "10010", "10001"],
    "L": ["10000", "10000", "10000", "10000", "10000", "10000", "11111"],
    "M": ["10001", "11011", "10101", "10101", "10001", "10001", "10001"],
    "N": ["10001", "11001", "10101", "10011", "10001", "10001", "10001"],
    "O": ["01110", "10001", "10001", "10001", "10001", "10001", "01110"],
    "P": ["11110", "10001", "10001", "11110", "10000", "10000", "10000"],
    "R": ["11110", "10001", "10001", "11110", "10100", "10010", "10001"],
    "S": ["01111", "10000", "10000", "01110", "00001", "00001", "11110"],
    "T": ["11111", "00100", "00100", "00100", "00100", "00100", "00100"],
    "U": ["10001", "10001", "10001", "10001", "10001", "10001", "01110"],
    "V": ["10001", "10001", "10001", "10001", "10001", "01010", "00100"],
    "W": ["10001", "10001", "10001", "10101", "10101", "10101", "01010"],
    "Y": ["10001", "10001", "01010", "00100", "00100", "00100", "00100"],
    " ": ["00000", "00000", "00000", "00000", "00000", "00000", "00000"],
    "-": ["00000", "00000", "00000", "11111", "00000", "00000", "00000"],
}

VARIANTS = [
    ("RED", (181, 71, 90)),
    ("ORANGE", (196, 106, 44)),
    ("AMBER", (184, 137, 45)),
    ("GREEN", (47, 143, 91)),
    ("CYAN", (47, 124, 143)),
    ("BLUE", (62, 111, 182)),
    ("VIOLET", (111, 91, 178)),
]

POSITIONS = [
    (100, 300),
    (528, 300),
    (956, 300),
    (1384, 300),
    (314, 686),
    (742, 686),
    (1170, 686),
]


def clamp(value):
    return max(0, min(255, int(value)))


class Canvas:
    def __init__(self, width, height, background):
        self.width = width
        self.height = height
        self.pixels = [[list(background) for _ in range(width)] for _ in range(height)]

    def fill_rect(self, x, y, width, height, color):
        for yy in range(y, y + height):
            if 0 <= yy < self.height:
                row = self.pixels[yy]
                for xx in range(max(0, x), min(self.width, x + width)):
                    row[xx] = [color[0], color[1], color[2]]

    def blend(self, x, y, color, alpha):
        if 0 <= x < self.width and 0 <= y < self.height:
            base = self.pixels[y][x]
            self.pixels[y][x] = [
                clamp(base[i] * (1 - alpha) + color[i] * alpha) for i in range(3)
            ]

    def round_rect(self, x, y, width, height, radius, color):
        radius_squared = radius * radius
        for yy in range(y, y + height):
            if not (0 <= yy < self.height):
                continue
            for xx in range(x, x + width):
                if not (0 <= xx < self.width):
                    continue
                inside = False
                if x + radius <= xx < x + width - radius:
                    inside = True
                elif y + radius <= yy < y + height - radius:
                    inside = True
                else:
                    corner_x = x + radius if xx < x + radius else x + width - radius - 1
                    corner_y = y + radius if yy < y + radius else y + height - radius - 1
                    dx = xx - corner_x
                    dy = yy - corner_y
                    inside = dx * dx + dy * dy <= radius_squared
                if inside:
                    self.pixels[yy][xx] = [color[0], color[1], color[2]]

    def shadow(self, x, y, width, height, spread, color=(30, 37, 44)):
        for yy in range(y - spread, y + height + spread):
            if not (0 <= yy < self.height):
                continue
            for xx in range(x - spread, x + width + spread):
                if not (0 <= xx < self.width):
                    continue
                dx = 0
                dy = 0
                if xx < x:
                    dx = x - xx
                elif xx >= x + width:
                    dx = xx - (x + width - 1)
                if yy < y:
                    dy = y - yy
                elif yy >= y + height:
                    dy = yy - (y + height - 1)
                distance = (dx * dx + dy * dy) ** 0.5
                if distance <= spread:
                    alpha = max(0.0, 0.12 * (1 - distance / spread))
                    self.blend(xx, yy, color, alpha)

    def draw_text(self, x, y, text, scale, color):
        cursor_x = x
        for char in text:
            glyph = FONT.get(char, FONT[" "])
            for glyph_y, row in enumerate(glyph):
                for glyph_x, bit in enumerate(row):
                    if bit == "1":
                        self.fill_rect(
                            cursor_x + glyph_x * scale,
                            y + glyph_y * scale,
                            scale,
                            scale,
                            color,
                        )
            cursor_x += 6 * scale

    def save_png(self, path):
        raw = bytearray()
        for row in self.pixels:
            raw.append(0)
            for r, g, b in row:
                raw.extend((r, g, b))

        def chunk(tag, data):
            return (
                struct.pack(">I", len(data))
                + tag
                + data
                + struct.pack(">I", zlib.crc32(tag + data) & 0xFFFFFFFF)
            )

        png = bytearray(b"\x89PNG\r\n\x1a\n")
        png += chunk(b"IHDR", struct.pack(">IIBBBBB", self.width, self.height, 8, 2, 0, 0, 0))
        png += chunk(b"IDAT", zlib.compress(bytes(raw), 9))
        png += chunk(b"IEND", b"")
        path.write_bytes(png)


def draw_background(canvas):
    for y in range(canvas.height):
        mix = y / canvas.height
        row_color = (
            int(BACKGROUND[0] - mix * 6),
            int(BACKGROUND[1] - mix * 4),
            int(BACKGROUND[2] - mix * 2),
        )
        canvas.fill_rect(0, y, canvas.width, 1, row_color)

    glows = [
        (181, 71, 90),
        (184, 137, 45),
        (47, 124, 143),
        (111, 91, 178),
    ]
    for index, glow in enumerate(glows):
        for y in range(130):
            alpha = 0.06 * (1 - y / 130)
            for x in range(canvas.width):
                if (x + index * 180) % 900 < 460:
                    canvas.blend(x, y, glow, alpha)


def draw_card(canvas, x, y, label, accent):
    canvas.shadow(x, y, 330, 300, 20)
    canvas.round_rect(x, y, 330, 300, 26, (255, 255, 255))
    canvas.round_rect(x, y, 330, 18, 18, accent)

    canvas.fill_rect(x + 26, y + 42, 84, 10, (48, 56, 64))
    chrome = [
        (x + 170, y + 36, 44, accent),
        (x + 226, y + 36, 34, (186, 193, 201)),
        (x + 270, y + 36, 34, (186, 193, 201)),
    ]
    for button_x, button_y, width, color in chrome:
        canvas.round_rect(button_x, button_y, width, 18, 9, color)

    canvas.round_rect(x + 24, y + 72, 282, 96, 18, (245, 247, 249))
    canvas.fill_rect(x + 24, y + 72, 282, 5, accent)
    canvas.fill_rect(x + 44, y + 98, 114, 8, (57, 65, 73))
    canvas.fill_rect(x + 44, y + 122, 178, 6, (110, 118, 126))
    canvas.fill_rect(x + 44, y + 140, 154, 6, (138, 146, 154))
    canvas.round_rect(x + 232, y + 102, 48, 32, 10, accent)

    canvas.round_rect(x + 24, y + 190, 78, 28, 14, accent)
    canvas.round_rect(x + 112, y + 190, 88, 28, 14, (243, 245, 247))
    canvas.round_rect(x + 210, y + 190, 96, 28, 14, (243, 245, 247))

    for offset_x in (24, 170):
        canvas.round_rect(x + offset_x, y + 234, 136, 42, 14, (249, 250, 251))
        canvas.fill_rect(x + offset_x + 16, y + 248, 70, 5, (90, 98, 106))
        canvas.fill_rect(x + offset_x + 16, y + 260, 92, 4, (150, 158, 166))

    canvas.draw_text(x + 24, y + 286, label, 4, accent)


def main():
    canvas = Canvas(WIDTH, HEIGHT, BACKGROUND)
    draw_background(canvas)

    canvas.shadow(88, 72, 1624, 180, 26)
    canvas.round_rect(88, 72, 1624, 180, 32, (255, 255, 255))
    canvas.draw_text(150, 128, "SPECTRUM THEME", 8, (31, 35, 40))
    canvas.draw_text(152, 194, "JENKINS INSTANCE PREVIEW", 4, (102, 112, 122))

    for (label, accent), (x, y) in zip(VARIANTS, POSITIONS):
        draw_card(canvas, x, y, label, accent)

    canvas.save_png(OUT)
    print(OUT)


if __name__ == "__main__":
    main()
