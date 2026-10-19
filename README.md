# TooMuchMaterials:

## Concept:

- Tech-based mod
- You need to make circuit boards, N/P type silicon, diodes/transistors/chips from basic materials
- But once you have something you can easily make more of it
- with new items you can craft machines, to automate ...
- i'll try to make it work with applied energistics, so you can go crazy ...

## Progessing:
- hard start, with expensive crafting recipes ➔ lots of materials ➔ few outcome
- gets progressively easier ➔ few resources ➔ lots of outcome
- endgame ➔ most things are basically free or/and can be duplicated

# How to run:

- a) run `.\gradlew build runClient` in the main folder
- b) copy [this](.\build\libs) (.\build\libs\tmm-x.x.x.jar) into your mods folder and launch minecraft



# Early Game

## Early Game phase 1

Objectives:
- Make level 1 circuit boards
- get your 1st cutting machine to passively produce wires, without wasting iron for new wire cutters
- generate power

### 2x iron_ingot + 3x sticks -> wire_cutter(item)

| | Crafting Grid | | | Output |
| :---: | :---: | :---: | :---: | :---: |
| ❌ | Iron Ingot | ❌ | | |
| Stick | Stick | Iron Ingot | ➔ |  **Wire Cutter** ✅|  
| ❌ | Stick | ❌ | | |

### wire_cutter: 1x copper_ingot -> 1x copper_wire

| | Crafting Shapeless | | Output |
| :---: | :---: | :---: | :---: |
| Wire Cutter | Iron Ingot | ➔ |  **Copper Wire** ✅|  


### 4x copper_wire + iron_ingot -> electro_magnet_bad(item)(poorly insulated)

| | Crafting Grid | | | Output |
| :---: | :---: | :---: | :---: | :---: |
| ❌ | Copper Wire | ❌ | | |
| Copper Wire | Iron Ingot | Copper Wire | ➔ |  **Electro Magnet** ✅|
| ❌ | Copper Wire | ❌ | | |


### 2x copper_wire + 1x electro_magnet(item) + 3x smooth_stone + 1x iron_ingot + 1x copper_ingot -> relais(item)

| | Crafting Grid | | | Output |
| :---: | :---: | :---: | :---: | :---: |
| ❌ | Iron Ingot | Copper Ingot | | |
| Copper Wire | Electro Magnet | Copper Wire | ➔ |  **Relais** ✅|
| Smooth Stone | Smooth Stone | Smooth Stone | | |

### copper_wire + relais + 3x smooth_stone + lever/button -> circuit_board_lv1(item)

| | Crafting Grid | | | Output |
| :---: | :---: | :---: | :---: | :---: |
| Copper Wire | Copper Wire | Copper Wire | | |
| Button/Lever | Relais | Copper Wire | ➔ |  **Circuit Board Lv.1** ✅|
| Smooth Stone | Smooth Stone | Smooth Stone | | |

### Iron Ingot + Iron Blocl -> turbine (item)

| | Crafting Grid | | | Output |
| :---: | :---: | :---: | :---: | :---: |
| ❌ | Iron Ingot | ❌ | | |
| Iron Ingot | Iron Block | Iron Ingot | ➔ |  **Turbine** ✅|
| ❌ | Iron Ingot | ❌ | | |

### electro motor (item)

| | Crafting Grid | | | Output |
| :---: | :---: | :---: | :---: | :---: |
| ❌ | Iron Ingot | ❌ | | |
| Iron Ingot | Electro Magnet | Iron Ingot | ➔ |  **Electro Motor** ✅|
| ❌ | Iron Ingot | Circuit Board Lv.1 | | |

### generator (block)

| | Crafting Grid | | | Output |
| :---: | :---: | :---: | :---: | :---: |
| ❌ | Turbine | Electro Motor | | |
| Hopper | Cauldron | Copper Wire | ➔ |  **Generator** ✅|
| Furnace | Copper Block | Furnace | | |

### 4x iron_blocks + wire_cutter + circuit_boardl_v1 +  2x hopper -> cutting_machine (block)

| | Crafting Grid | | | Output |
| :---: | :---: | :---: | :---: | :---: |
| Iron Block | Hopper | Iron Block | | |
| ❌ | Wire Cutter | Circuit Board Lv.1 | ➔ |  **Cutting Machine** |
| Iron Block | Hopper | Iron Block | | |

## Early Game phase 2

Objectives:
- get and automate level 2 circuit boards
    - level 2 circuit boards require components like transistors and diodes
- ...

Quarz  Coal-> smelting -> Silicon + CO² (for now CO² just makes smoke particles)

Doping process:
- Raw Silicon + Redstone Block -> N-type Silicon (red)
- Raw Silicon Soul Sand -> P-type Silicon (blue/black)
- thermo diffusion for doping process (super heating)

### 8x glass + quartz -> quartz glass

| | Crafting Grid | | | Output |
| :---: | :---: | :---: | :---: | :---: |
| Glass | Glass | Glass | | |
| Glass | Quartz | Glass | ➔ | 8x **Quartz Glass** |
| Glass | Glass | Glass | | |

### netherite + copper wire -> Superheating coil

| | Crafting Grid | | | Output |
| :---: | :---: | :---: | :---: | :---: |
| Netherite Scrap | Netherite Scrap | Netherite Scrap | | |
| Netherite Scrap | ❌ | Netherite Scrap | ➔ | 2x **Superheating Coil** |
| Copper Wire | ❌ | Copper Wire | | |

###  quartz glass + netherite scraps -> High-Temperature Pressure Vessel

| | Crafting Grid | | | Output |
| :---: | :---: | :---: | :---: | :---: |
| Netherite Scrap | Quartz Glass | Netherite Scrap | | |
| Quartz Glass | ❌ | Quartz Glass | ➔ | **High-Temperature Pressure Vessel** |
| Netherite Scrap | Quartz Glass | Netherite Scrap | | |

### quarz glass + superheating coil -> diffusion camber

| | Crafting Grid | | | Output |
| :---: | :---: | :---: | :---: | :---: |
| ❌ | Quartz Glass | ❌ | | |
| Quartz Glass | ❌ | Quartz Glass | ➔ | **Diffusion Furnace** |
| Circuit Board Lv.1 | Superheating Coil | ❌ | | |

### N-type Silicon + P-type Silicon + Copper Wire + Glass -> Diode

| | Crafting Grid | | | Output |
| :---: | :---: | :---: | :---: | :---: |
| ❌ | P-type Silicon | ❌ | | |
| Copper Wire | N-type Silicon | Copper Wire | ➔ |  4x **Diode** |
| ❌ | Glass | ❌ | | |
