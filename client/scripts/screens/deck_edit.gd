extends CanvasItem

@onready var user = get_node("/root/User")
@onready var cards_tilemap = %TileMap

var points_dict = {0: Vector2(0,0), 1: Vector2(1,0), 
				   2: Vector2(2,0), 3: Vector2(0,1), 
				   4: Vector2(1,1), 5: Vector2(2,1), 
				   6: Vector2(1,2)
				  }

func _ready():
	build_deck()

func build_deck():
	Tilemap_Utils.build_empty_in_coords(cards_tilemap, points_dict)
	for i in points_dict:
		var tile = Tilemap_Utils.get_tile(cards_tilemap, points_dict[i])
		set_card_to_cell(tile, user.deck[i])
	cards_tilemap.update_internals()

func set_card_to_cell(cell, card : Card):
	cell.card_data = card
	cell.panel_position = Vector2(-40,-33.5)
	cell.update_all()
