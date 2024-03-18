extends PanelContainer

@onready var dictionary_utils = get_node("/root/DictionaryUtils")
@onready var node_utils = get_node("/root/NodeUtils")
@onready var tilemap = $BoardTileMap
var board: Array
var points_dict : Dictionary = {0: Vector2i(0,0), 1: Vector2i(1,0), 2: Vector2i(2,0),
								3: Vector2i(0,1), 4: Vector2i(1,1), 5: Vector2i(2,1),
								6: Vector2i(0,2), 7: Vector2i(1,2), 8: Vector2i(2,2),
								9: Vector2i(0,3), 10 : Vector2i(1,3), 11: Vector2i(2,3),
												12: Vector2i(1,4)
								}

signal board_clicked(coord : Vector2i)

## BUG MIGHT HERE!
func build_board():
	for i in points_dict:
		tilemap.set_cell(0, points_dict[i], 0, Vector2i(0,0), 1)
	tilemap.update_internals()
	for i in points_dict:
		var col = points_dict[i].x
		var row = points_dict[i].y
		var in_game_card : InGameCard = board[col][row]
		set_card_to_cell(tilemap.get_child(i), in_game_card)
	tilemap.update_internals()

func set_card_to_cell(cell, in_game_card : InGameCard):
	cell.panel_position = Vector2(-40,-33.5)
	if in_game_card.exists:
		cell.card_data = in_game_card.card
		cell.exists = true
	else:
		cell.exists = false
	cell.update_all()

func _input(event):
	if Input.is_action_just_pressed("click"):
		var clicked_tile : Vector2i = tilemap.local_to_map(get_local_mouse_position())
		if dictionary_utils.dictionary_has_item(points_dict,clicked_tile):
			board_clicked.emit(clicked_tile)
