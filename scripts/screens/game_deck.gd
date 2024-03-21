extends Control

@onready var dictionary_utils = get_node("/root/DictionaryUtils")
@onready var tilemap_utils = get_node("/root/TilemapUtils")
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
	tilemap_utils.build_empty_in_coords(deck_tiles, points_dict)
	for i in points_dict:
		var tile = tilemap_utils.get_tile(deck_tiles, points_dict[i])
		set_card_to_cell(tile, deck[i])
	deck_tiles.update_internals()

func set_card_to_cell(cell, card : InGameCard):
	cell.panel_position = Vector2(-40,-33.5)
	if card.exists:
		cell.in_board = false
		cell.card_data = card.card
		cell.exists = true
	else:
		cell.set_selection(false)
		cell.exists = false
	cell.update_all()

func update_selected(new_index : int = -1, old_index : int = -1):
	if old_index != -1:
		var cell = tilemap_utils.get_tile(deck_tiles, points_dict[old_index])
		cell.set_selection(false)
	if new_index != -1:
		var cell = tilemap_utils.get_tile(deck_tiles, points_dict[new_index])
		cell.set_selection(true)
	build_deck()

func get_card_data_from_index(index : int) -> Card:
	return tilemap_utils.get_tile(deck_tiles, points_dict[index]).card_data

func has_card(coords : Vector2i) -> bool:
	return dictionary_utils.dictionary_has_item(points_dict,coords)

func _input(event):
	if Input.is_action_just_pressed("click"):
		var clicked_tile : Vector2i = deck_tiles.local_to_map(get_local_mouse_position())
		if has_card(clicked_tile):
			var index = points_dict.find_key(clicked_tile)
			card_selected.emit(index)
