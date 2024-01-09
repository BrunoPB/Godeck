extends PanelContainer

@onready var main_menu_paths = get_node("/root/Enums").main_menu_paths
@onready var menu_screen_enum = get_node("/root/Enums").menu_screen_enum
@onready var utils = get_node("/root/Utils")
@onready var home_screen_scene = preload("res://scenes/home_screen.tscn").instantiate()

# Called when the node enters the scene tree for the first time.
func _ready():
	change_menu_screen(menu_screen_enum.HOME)

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(delta):
	pass

func change_menu_screen(screen):
	release_menu_buttons()
	var content_area = get_node(main_menu_paths["content_area"])
	utils.remove_children(content_area)
	match screen:
		menu_screen_enum.HOME:
			content_area.add_child(home_screen_scene)
			get_node(main_menu_paths["home_button"]).toggle_mode = true
		menu_screen_enum.SHOP:
			content_area.add_child(home_screen_scene)
			get_node(main_menu_paths["shop_button"]).toggle_mode = true
		menu_screen_enum.DECKBUILDER:
			content_area.add_child(home_screen_scene)
			get_node(main_menu_paths["deck_builder_button"]).toggle_mode = true
		menu_screen_enum.FRIENDS:
			content_area.add_child(home_screen_scene)
			get_node(main_menu_paths["friends_button"]).toggle_mode = true
		menu_screen_enum.EVENTS:
			content_area.add_child(home_screen_scene)
			get_node(main_menu_paths["events_button"]).toggle_mode = true

func release_menu_buttons():
	get_node(main_menu_paths["shop_button"]).toggle_mode = false
	get_node(main_menu_paths["deck_builder_button"]).toggle_mode = false
	get_node(main_menu_paths["home_button"]).toggle_mode = false
	get_node(main_menu_paths["friends_button"]).toggle_mode = false
	get_node(main_menu_paths["events_button"]).toggle_mode = false
