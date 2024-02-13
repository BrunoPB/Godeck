extends CanvasItem

@onready var card_utils = get_node("/root/CardUtils")
@onready var user = get_node("/root/User")
@onready var cards_tilemap = $TileMap

var points_dict = {0: Vector2(0,0), 1: Vector2(1,0), 
				   2: Vector2(2,0), 3: Vector2(0,1), 
				   4: Vector2(1,1), 5: Vector2(2,1), 
				   6: Vector2(1,2)
				  }

func _ready():
	build_deck()

func build_deck():
	for i in range(7):
		cards_tilemap.set_cell(0, points_dict.get(i), 0, Vector2i(0,0), 1)
	cards_tilemap.update_internals()
	for i in range(7):
		set_card_to_cell(cards_tilemap.get_child(i), user.deck[i])

func set_card_to_cell(cell, card : Card):
	cell.card_data = card
	cell.panel_position = Vector2(-40,-33.5)
	cell.update_all()

'''
--- Future TODOS ---
. The deck is not an immutable thing, it has to be able to change with user 
input and with such a need, there needs to be methods to edit deck post it's
initial setup. That means connecting signals from the UI elements to make 
the deck dynamic.
--- OBS ---
. set_cell function is a bit bizzare and works weirdly (i.e. placing tiles and
scenes in code works quite differently???), make sure to check documentation
if you plan on using/changing it (it is at least properly documented).
'''
