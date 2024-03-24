extends Node2D

@onready var s_panel : Node = %Layout
@onready var s_hex : Node = %CardHex
@onready var s_stats : Node = %Stats
@onready var s_border : Node = %Border

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
	s_panel.position = panel_position

func update_size():
	pass

func update_texture():
	var tex = Texture2D.new()
	if exists:
		s_hex.texture = load("res://assets/placeholders/Boitata.jpg")
	else:
		s_hex.texture = tex.create_placeholder()

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
	s_border.default_color = color

func update_stats():
	## FIND BUG HERE!
	s_stats.get_node("North").text = str(card_data.north) if exists else ""
	s_stats.get_node("NorthSide/NorthWest").text = str(card_data.north_west) if exists else ""
	s_stats.get_node("NorthSide/NorthEast").text = str(card_data.north_east) if exists else ""
	s_stats.get_node("SouthSide/SouthWest").text = str(card_data.south_west) if exists else ""
	s_stats.get_node("SouthSide/SouthEast").text = str(card_data.south_east) if exists else ""
	s_stats.get_node("South").text = str(card_data.south) if exists else ""

func set_selection(v : bool = true):
	selected = v
	z_index = 99 if v else 0
