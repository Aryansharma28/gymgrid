"""Render the illustrated README preview from the app's dumbbell asset."""

from pathlib import Path
from PIL import Image, ImageDraw, ImageFont

ROOT = Path(__file__).resolve().parents[1]
OUT = ROOT / "docs" / "preview.png"
ASSET = ROOT / "app" / "src" / "main" / "res" / "drawable-nodpi" / "gym_dumbbell.png"

BG = "#07140d"
CARD = "#11271a"
EDGE = "#2b4b33"
WHITE = "#eff9f0"
SUB = "#9ab7a3"
GREEN = "#43cc75"
CORAL = "#ff916f"
EMPTY = "#23402d"

font_dir = Path("C:/Windows/Fonts")
regular = font_dir / "segoeui.ttf"
bold = font_dir / "segoeuib.ttf"
if not regular.exists():
    regular = Path("/usr/share/fonts/truetype/dejavu/DejaVuSans.ttf")
    bold = Path("/usr/share/fonts/truetype/dejavu/DejaVuSans-Bold.ttf")


def font(size, heavy=False):
    return ImageFont.truetype(str(bold if heavy else regular), size)


image = Image.new("RGB", (1100, 760), BG)
d = ImageDraw.Draw(image)
d.ellipse((815, -160, 1270, 300), fill="#0d2819")
d.ellipse((-170, 430, 320, 920), fill="#0b2417")


def box(xy, radius, fill, outline=None, width=1):
    d.rounded_rectangle(xy, radius, fill=fill, outline=outline, width=width)


def label(xy, value, size, color=WHITE, heavy=False):
    d.text(xy, value, fill=color, font=font(size, heavy))


def asset(xy, side):
    art = Image.open(ASSET).convert("RGBA")
    art.thumbnail((side, side), Image.Resampling.LANCZOS)
    image.paste(art, xy, art)


box((76, 28, 516, 734), 55, "#020704", "#44654e", 4)
box((91, 44, 501, 719), 42, BG)
box((235, 55, 355, 72), 9, "#020704")
asset((116, 76), 40)
label((162, 77), "GymGrid", 24, heavy=True)
label((358, 84), "PRIVATE BY DESIGN", 10, SUB, True)
label((118, 132), "T H E  D A I L Y  P R A C T I C E", 11, GREEN, True)
label((116, 157), "Every day adds up.", 39, WHITE, True)
label((118, 205), "One honest answer. A stronger habit.", 15, SUB)

box((115, 246, 477, 400), 29, "#175435", "#427e50")
d.ellipse((337, 260, 477, 399), fill="#1d6240")
label((139, 261), "TODAY", 11, "#b2edbb", True)
label((138, 286), "Did you train", 28, WHITE, True)
label((138, 318), "today?", 28, WHITE, True)
box((138, 359, 291, 386), 14, "#346a43")
label((151, 363), "Ready when you are", 12, WHITE, True)
asset((327, 249), 151)

box((115, 416, 287, 498), 21, CARD, EDGE)
box((302, 416, 477, 498), 21, CARD, EDGE)
label((133, 430), "DAYS TRAINED", 11, SUB, True)
label((133, 450), "12", 32, WHITE, True)
label((320, 430), "CURRENT STREAK", 11, SUB, True)
label((320, 450), "4", 32, GREEN, True)

box((115, 513, 477, 672), 25, CARD, EDGE)
label((138, 526), "October 2026", 22, WHITE, True)
label((409, 526), "‹  ›", 25, "#a8e6b4")
for row in range(2):
    for col in range(7):
        x, y = 138 + col * 42, 560 + row * 40
        color = GREEN if (row, col) in {(0, 0), (0, 1), (0, 3), (0, 5), (1, 2), (1, 4)} else CORAL if (row, col) in {(0, 2), (1, 6)} else EMPTY
        box((x, y, x + 32, y + 32), 8, color)

box((91, 674, 501, 719), 22, "#0f2418")
box((115, 680, 288, 714), 17, GREEN)
box((301, 680, 477, 714), 17, CORAL)
label((183, 684), "YES", 17, BG, True)
label((374, 684), "NO", 17, BG, True)

label((590, 83), "G Y M G R I D  ·  D E S I G N  P R E V I E W", 16, "#a8e6b4", True)
label((590, 114), "Your effort, in color.", 44, WHITE, True)
label((590, 167), "One answer a day. Your widgets, your history.", 20, SUB)

label((590, 251), "M O N T H  W I D G E T", 15, "#a8e6b4", True)
box((589, 283, 1029, 453), 28, "#0d2418", "#346846")
label((614, 293), "GymGrid", 24, WHITE, True)
label((614, 329), "Recent days · 2 columns", 14, SUB)
for row in range(4):
    for col in range(2):
        x, y = 613 + col * 91, 354 + row * 23
        color = GREEN if (row, col) in {(0, 0), (1, 1), (2, 0), (3, 1)} else CORAL if (row, col) in {(0, 1), (2, 1)} else EMPTY
        box((x, y, x + 82, y + 19), 5, color)
        label((x + 7, y + 1), f"{'MTWTFSS'[row + col]} {21 - row * 2 - col}", 12, BG if color != EMPTY else WHITE, True)
box((825, 365, 907, 414), 17, GREEN)
box((917, 365, 999, 414), 17, CORAL)
label((846, 374), "YES", 16, BG, True)
label((943, 374), "NO", 16, BG, True)

label((590, 507), "Y E A R  W I D G E T", 15, "#a8e6b4", True)
box((589, 536, 1029, 713), 28, "#0d2418", "#346846")
label((614, 547), "The long view", 24, WHITE, True)
label((614, 583), "A little stronger every day.", 13, SUB)
for row in range(4):
    for col in range(12):
        x, y = 614 + col * 22, 613 + row * 22
        color = GREEN if (row * 11 + col * 7) % 5 == 0 else CORAL if (row * 7 + col * 3) % 11 == 0 else EMPTY
        box((x, y, x + 16, y + 16), 4, color)
box((895, 622, 1002, 654), 12, GREEN)
box((895, 661, 1002, 693), 12, CORAL)
label((930, 625), "YES", 14, BG, True)
label((935, 664), "NO", 14, BG, True)

image.save(OUT, optimize=True)
