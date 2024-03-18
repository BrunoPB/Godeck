extends Control

@onready var card_utils = get_node("/root/CardUtils")
@onready var dictionary_utils = get_node("/root/DictionaryUtils")
@onready var node_utils = get_node("/root/NodeUtils")
@onready var deck_tiles = $DeckTiles
var deck : Array

signal card_selected(index : int)

var points_dict : Dictionary = {0: Vector2i(0,0), 1: Vector2i(1,0), 
								2: Vector2i(2,0), 3: Vector2i(3,0), 
								4: Vector2i(4,0), 5: Vector2i(1,1), 
								6: Vector2i(3,1)
								}

func _ready():
	pass

func build_deck():
	for i in points_dict:
		deck_tiles.set_cell(0, points_dict[i], 0, Vector2i(0,0), 1)
	deck_tiles.update_internals()
	for i in points_dict:
		set_card_to_cell(deck_tiles.get_child(i), deck[i])
	deck_tiles.update_internals()

func set_card_to_cell(cell, card : InGameCard):
	cell.panel_position = Vector2(-40,-33.5)
	if card.exists:
		cell.card_data = card.card
		cell.exists = true
	else:
		cell.exists = false
	cell.update_all()

func get_card_data_from_index(index : int) -> Card:
	return deck_tiles.get_child(index).card_data

func has_card(coords : Vector2i) -> bool:
	return dictionary_utils.dictionary_has_item(points_dict,coords)

func _input(event):
	if Input.is_action_just_pressed("click"):
		var clicked_tile : Vector2i = deck_tiles.local_to_map(get_local_mouse_position())
		if has_card(clicked_tile):
			var index = points_dict.find_key(clicked_tile)
			card_selected.emit(index)
