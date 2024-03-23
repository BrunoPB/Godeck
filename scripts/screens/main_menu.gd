extends PanelContainer

@onready var menu_screen_enum = get_node("/root/Constants").menu_screen_enum
@onready var home_screen_scene = preload("res://scenes/main_menu/home_screen.tscn")
@onready var deck_builder_screen_scene = preload("res://scenes/main_menu/deck_builder/deck_builder_screen.tscn")
@onready var shop_screen_scene = preload("res://scenes/main_menu/shop/shop_screen.tscn")

@onready var s_shop_button = %VerticalLayout/Menu/MenuLayout/ShopButton
@onready var s_deck_builder_button = %VerticalLayout/Menu/MenuLayout/DeckBuilderButton
@onready var s_home_button = %VerticalLayout/Menu/MenuLayout/HomeButton
@onready var s_friends_button = %VerticalLayout/Menu/MenuLayout/FriendsButton
@onready var s_events_button = %VerticalLayout/Menu/MenuLayout/EventsButton
@onready var s_content_area = %VerticalLayout/ContentArea

# Called when the node enters the scene tree for the first time.
func _ready():
	change_menu_screen(menu_screen_enum.HOME)

# Called every frame. 'delta' is the elapsed time since the previous frame.
func _process(_delta):
	pass

func change_menu_screen(screen):
	release_menu_buttons()
	var content_area = s_content_area
	Node_Utils.remove_children(content_area)
	match screen:
		menu_screen_enum.HOME:
			content_area.add_child(home_screen_scene.instantiate())
			s_home_button.button_pressed = true
		menu_screen_enum.SHOP:
			content_area.add_child(shop_screen_scene.instantiate())
			s_shop_button.button_pressed = true
		menu_screen_enum.DECKBUILDER:
			content_area.add_child(deck_builder_screen_scene.instantiate())
			s_deck_builder_button.button_pressed = true
		menu_screen_enum.FRIENDS:
			content_area.add_child(home_screen_scene.instantiate())
			s_friends_button.button_pressed = true
		menu_screen_enum.EVENTS:
			content_area.add_child(home_screen_scene.instantiate())
			s_events_button.button_pressed = true

func release_menu_buttons():
	s_shop_button.button_pressed = false
	s_deck_builder_button.button_pressed = false
	s_home_button.button_pressed = false
	s_friends_button.button_pressed = false
	s_events_button.button_pressed = false

func _on_home_button_button_up():
	change_menu_screen(menu_screen_enum.HOME)

func _on_deck_builder_button_button_up():
	change_menu_screen(menu_screen_enum.DECKBUILDER)

func _on_shop_button_button_up():
	change_menu_screen(menu_screen_enum.SHOP)

func _on_friends_button_button_up():
	change_menu_screen(menu_screen_enum.FRIENDS)

func _on_events_button_button_up():
	change_menu_screen(menu_screen_enum.EVENTS)
