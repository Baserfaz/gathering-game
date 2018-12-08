package com.data;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.enumerations.BlockType;
import com.enumerations.Direction;
import com.enumerations.SpriteType;
import com.gameobjects.Block;
import com.utilities.Util;

public class Level {

    private List<Block> blocks = new ArrayList<>();
    
    private int height;
    private int width;
    
    public Level(int height, int width) {
        this.height = height;
        this.width = width;

        this.createInitialLevel();
    }

    private void createInitialLevel() {

        // create floor
        for(int y = 0; y < this.height; y++) {
            for(int x = 0; x < this.width; x++) {
                Point tilePos = new Point(x, y);
                Block block = new Block(tilePos, BlockType.WALKABLE, SpriteType.FLOOR);
                blocks.add(block);
            }
        }

        ArrayList<Block> floors = new ArrayList<>(blocks);

        // create north walls
        for (Block floor : floors) {

            // walls have two parts to the north
            Point tilepos = floor.getTilePosition();

            Block b1 = this.getBlock(new Point(tilepos.x, tilepos.y - 1));
            Block b2 = this.getBlock(new Point(tilepos.x, tilepos.y - 2));

            if(b1 == null && b2 == null) {
                blocks.add(new Block(new Point(tilepos.x, tilepos.y - 1), BlockType.UNWALKABLE, SpriteType.NORTH_WALL));
                blocks.add(new Block(new Point(tilepos.x, tilepos.y - 2), BlockType.UNWALKABLE, SpriteType.NORTH_WALL_TOP));
            }
        }

        // create w & e walls
        for(Block floor : floors) {
            Point tilepos = floor.getTilePosition();
            Block bw = this.getBlock(new Point(tilepos.x - 1, tilepos.y));
            Block be = this.getBlock(new Point(tilepos.x + 1, tilepos.y));

            if(bw == null) {
                blocks.add(new Block(new Point(tilepos.x - 1, tilepos.y), BlockType.UNWALKABLE, SpriteType.WEST_WALL));
            }

            if(be == null) {
                blocks.add(new Block(new Point(tilepos.x + 1, tilepos.y), BlockType.UNWALKABLE, SpriteType.EAST_WALL));
            }
        }

        // create south walls
        for(Block floor : floors) {
            Point tilepos = floor.getTilePosition();
            Block b = this.getBlock(new Point(tilepos.x, tilepos.y + 1));
            if(b == null) {
                blocks.add(new Block(new Point(tilepos.x, tilepos.y + 1), BlockType.UNWALKABLE, SpriteType.SOUTH_WALL));
            }
        }

        // do corners last

        // create sw & se walls
        for(Block floor : floors) {
            Point tilepos = floor.getTilePosition();
            Block bsw = this.getBlock(new Point(tilepos.x - 1, tilepos.y + 1));
            Block bse = this.getBlock(new Point(tilepos.x + 1, tilepos.y + 1));
            if(bsw == null) {
                blocks.add(new Block(new Point(tilepos.x - 1, tilepos.y + 1), BlockType.UNWALKABLE, SpriteType.SW_WALL));
            }
            if(bse == null) {
                blocks.add(new Block(new Point(tilepos.x + 1, tilepos.y + 1), BlockType.UNWALKABLE, SpriteType.SE_WALL));
            }
        }

        // create nw & ne walls
        for(Block floor : floors) {

            Point tilepos = floor.getTilePosition();

            Block bnw = this.getBlock(new Point(tilepos.x - 1, tilepos.y - 2));
            Block bne = this.getBlock(new Point(tilepos.x + 1, tilepos.y - 2));

            if(bnw == null) {
                blocks.add(new Block(new Point(tilepos.x - 1, tilepos.y - 2), BlockType.UNWALKABLE, SpriteType.NW_WALL));
            }

            if(bne == null) {
                blocks.add(new Block(new Point(tilepos.x + 1, tilepos.y - 2), BlockType.UNWALKABLE, SpriteType.NE_WALL));
            }

        }
    }

    /**
     * Doesn't return null blocks, so if direction is in the result set then there is neighbor.
     * @param block
     * @return
     */
    public HashMap<Direction, Block> getNeighbors(Block block) {
        HashMap<Direction, Block> ns = new HashMap<>();
        for(Direction dir : Direction.values()) {
            Block b = this.getNeighbor(block, dir);
            if(b == null) continue;
            ns.put(dir, b);
        }
        return ns;
    }

    public boolean validateBlock(Block block) {
        if(block == null) return false;
        if(block.getBlocktype().equals(BlockType.UNWALKABLE)) return false;
        return true;
    }

    public Block getNeighbor(Block block, Direction dir) {
        
        int x = block.getTilePosition().x;
        int y = block.getTilePosition().y;
        
        switch(dir) {
            case EAST:
                x += 1;
                break;
            case NORTH:
                y -= 1;
                break;
            case NORTH_EAST:
                y -= 1;
                x += 1;
                break;
            case NORTH_WEST:
                y -= 1;
                x -= 1;
                break;
            case SOUTH:
                y += 1;
                break;
            case SOUTH_EAST:
                y += 1;
                x += 1;
                break;
            case SOUTH_WEST:
                y += 1;
                x -= 1;
                break;
            case WEST:
                x -= 1;
                break;
        }
        return this.getBlock(new Point(x, y));
    }
    
    public Block getBlock(Point pos) {
        Block block = null;
        
        for(Block b : this.blocks) {
            if(b.getTilePosition().equals(pos)) {
                block = b;
                break;
            }
        }
        
        return block;
    }
    
    public Block getRandomValidBlock() {
        List<Block> bs = new ArrayList<>();
        for(Block b : this.blocks) {
            if(this.validateBlock(b)) { bs.add(b); }
        }
        return this.getRandomBlock(bs);
    }
    
    public List<Block> getBlocksOfType(BlockType type) {
        List<Block> bs = new ArrayList<>();
        
        for(Block b : this.blocks) {
            if(b.getBlocktype() == type) {
                bs.add(b);
            }
        }
        
        return bs;
    }
    
    public Block getRandomBlockOfType(BlockType type) {
        List<Block> bs = new ArrayList<>();
        
        for(Block b : this.blocks) {
            if(b.getBlocktype() == type) {
                bs.add(b);
            }
        }
        
        return this.getRandomBlock(bs);
    }
    
    public Block getRandomBlock(List<Block> bs) {
        return bs.get(Util.GetRandomInteger(0, bs.size() - 1));
    }
    
    public Block getRandomBlock() {
        return this.blocks.get(Util.GetRandomInteger(0, blocks.size() - 1));
    }
    
    public List<Block> getBlocks() {
        return this.blocks;
    }
    
    
}
