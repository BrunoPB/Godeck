extends Node2D

@onready var paths = get_node("/root/Constants").card_base_paths
var card_data : Card = Card.new()
var in_board : bool = false
var dominated : bool
var selected : bool = false
var radius : int = 40
var panel_position : Vector2 = Vector2(0,0)
var exists : bool = true

func _ready():
	pass

func update_all():
	update_panel_position()
	update_size()
	update_texture()
	update_border()
	update_stats()

func update_panel_position():
	get_node(paths["panel"]).position = panel_position

func update_size():
	pass

func update_texture():
	var tex = Texture2D.new()
	if exists:
		$Layout/CardHex.texture = load("res://assets/placeholders/Boitata.jpg")
	else:
		$Layout/CardHex.texture = tex.create_placeholder()

func update_border():
	var color : Color
	if not exists:
		color = Color.BLACK
	elif selected:
		color = Color.WHITE_SMOKE
	elif in_board and dominated:
		color = Color.WEB_GREEN
	elif in_board and not dominated:
		color = Color.DARK_RED
	$Layout/CardHex/Border.default_color = color

func update_stats():
	get_node(paths["stats"] + "/North").text = str(card_data.north) if exists else ""
	get_node(paths["stats"] + "/NorthSide/NorthWest").text = str(card_data.north_west) if exists else ""
	get_node(paths["stats"] + "/NorthSide/NorthEast").text = str(card_data.north_east) if exists else ""
	get_node(paths["stats"] + "/SouthSide/SouthWest").text = str(card_data.south_west) if exists else ""
	get_node(paths["stats"] + "/SouthSide/SouthEast").text = str(card_data.south_east) if exists else ""
	get_node(paths["stats"] + "/South").text = str(card_data.south) if exists else ""

func set_selection(v : bool = true):
	selected = v
	z_index = 99 if v else 0
