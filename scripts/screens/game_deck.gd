extends Control

@onready var card_utils = get_node("/root/CardUtils")
@onready var user = get_node("/root/User")
@onready var deck_tiles = $DeckTiles

var points_dict = {0: Vector2(0,0), 1: Vector2(1,0), 
				   2: Vector2(2,0), 3: Vector2(3,0), 
				   4: Vector2(4,0), 5: Vector2(1,1), 
				   6: Vector2(3,1)
				  }

func _ready():
	build_deck()

func build_deck():
	for i in range(7):
		deck_tiles.set_cell(0, points_dict.get(i), 0, Vector2i(0,0), 1)
	deck_tiles.update_internals()
	for i in range(7):
		set_card_to_cell(deck_tiles.get_child(i), user.deck[i])

func set_card_to_cell(cell, card : Card):
	cell.card_data = card
	cell.panel_position = Vector2(-40,-33.5)
	cell.update_all()
