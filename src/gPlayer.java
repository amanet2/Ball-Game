import java.awt.geom.Rectangle2D;

public class gPlayer extends gThing {
    gThing collidedPlayer = null;
    String attackTargetType = "THING_PLAYER";
    String attackTargetId = "null";
    String decorationSprite = "null";
    int weapon = gWeapons.none;
    long botThinkTime = 0;
    long botShootTime = 0;
    boolean botGoAround = false;
    int botGoAroundDelay = 1500;
    long botGoAroundTick = 0;
    int[] botGoAroundLastCoords = coords.clone();
    int botGoAroundRadius = 600;

    public int getDistanceToThing(gThing target) {
        int x1 = coords[0];
        int y1 = coords[1];
        int x2 = target.coords[0];
        int y2 = target.coords[1];
        return (int) Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1, 2));
    }

    public void attackClosestTargetThing() {
        String thingType = attackTargetType;
        if(xMain.shellLogic.serverScene.getThingMap(thingType) == null)
            return;
        gThing closest = null;
        int closestDist = 1000000;
        if(xMain.shellLogic.serverScene.getThingMap(thingType).containsKey(attackTargetId)) {
           closest = xMain.shellLogic.serverScene.getThingMap(thingType).get(attackTargetId);
           closestDist = getDistanceToThing(closest);
        }
        else {
            for (String oid : xMain.shellLogic.serverScene.getThingMap(thingType).keySet()) {
                if (id.equals(oid))
                    continue;
                gThing dst = xMain.shellLogic.serverScene.getThingMap(thingType).get(oid);
                int dist = getDistanceToThing(dst);
                if (dist < closestDist) {
                    closest = dst;
                    closestDist = dist;
                }
            }
        }
        if(closest != null) {
            if(!botGoAround) {
                if(Math.random() < 0.5) {
                    if (closest.coords[1] > coords[1]) {
                        mov0 = 0;
                        mov1 = 1;
                    } else if (closest.coords[1] < coords[1]) {
                        mov0 = 1;
                        mov1 = 0;
                    } else {
                        mov0 = 0;
                        mov1 = 0;
                    }
                }
                if(Math.random() < 0.5) {
                    if (closest.coords[0] > coords[0]) {
                        mov2 = 0;
                        mov3 = 1;
                    } else if (closest.coords[0] < coords[0]) {
                        mov2 = 1;
                        mov3 = 0;
                    } else {
                        mov2 = 0;
                        mov3 = 0;
                    }
                }
            }
            //point at target
            double bdx = closest.coords[0] + closest.dims[0]/2 - coords[0] + dims[0]/2;
            double bdy = closest.coords[1] + closest.dims[1]/2 - coords[1] + dims[1]/2;
            double angle = Math.atan2(bdy, bdx);
            if (angle < 0)
                angle += 2*Math.PI;
            angle += Math.PI/2;
            double randomOffset = Math.random()*3;
            if(randomOffset > 2)
                angle -= Math.PI/8;
            else if(randomOffset > 1)
                angle += Math.PI/8;
            fv = angle;
            //attack
            if(closestDist < sSettings.botShootRange && botShootTime < sSettings.gameTime) {
                botShootTime = sSettings.gameTime + gWeapons.fromCode(weapon).refiredelay;
                xMain.shellLogic.serverNetThread.addNetCmd("server", String.format("fireweapon %s %d", id, weapon));
                xMain.shellLogic.serverNetThread.addIgnoringNetCmd("server", String.format("cl_fireweapon %s %d", id, weapon));
            }
            //check go around
            double travelDist = Math.sqrt(Math.pow(coords[0] - botGoAroundLastCoords[0],2) + Math.pow(coords[1] - botGoAroundLastCoords[1],2));
            if(sSettings.gameTime > botGoAroundTick) {
                botGoAround = false;
                botGoAroundTick = sSettings.gameTime + botGoAroundDelay;
                botGoAroundLastCoords = coords.clone();
                System.out.println("BOT_" + id + " traveled " + travelDist);
                if(travelDist < botGoAroundRadius) {
                    botGoAround = true;
                    System.out.println("BOT_" + id + " GO AROUND");
                    if(Math.random() < 0.5) {
                        if (closest.coords[1] > coords[1]) {
                            mov0 = 0;
                            mov1 = 1;
                        } else if (closest.coords[1] < coords[1]) {
                            mov0 = 1;
                            mov1 = 0;
                        } else {
                            mov0 = 0;
                            mov1 = 0;
                        }
                        if (closest.coords[0] > coords[0]) {
                            mov2 = 1; //go around val
                            mov3 = 0; //go around val
                        } else if (closest.coords[0] < coords[0]) {
                            mov2 = 0; //go around val
                            mov3 = 1; //go around val
                        } else {
                            mov2 = 0;
                            mov3 = 0;
                        }
                    }
                    else {
                        if (closest.coords[1] > coords[1]) {
                            mov0 = 1; //go around val
                            mov1 = 0; //go around val
                        } else if (closest.coords[1] < coords[1]) {
                            mov0 = 0; //go around val
                            mov1 = 1; //go around val
                        } else {
                            mov0 = 0;
                            mov1 = 0;
                        }
                        if (closest.coords[0] > coords[0]) {
                            mov2 = 0;
                            mov3 = 1;
                        } else if (closest.coords[0] < coords[0]) {
                            mov2 = 1;
                            mov3 = 0;
                        } else {
                            mov2 = 0;
                            mov3 = 0;
                        }
                    }
                }
            }
        }
    }

    public boolean wontClipOnMove(int dx, int dy, gScene scene) {
        for(String id : scene.getThingMap("BLOCK_COLLISION").keySet()) {
            gThing coll = scene.getThingMap("BLOCK_COLLISION").get(id);
            Rectangle2D playerRect = new Rectangle2D.Double(dx, dy, dims[0], dims[1]);
            Rectangle2D collRect = new Rectangle2D.Double(coll.coords[0], coll.coords[1], coll.dims[0], coll.dims[1]);
            if(playerRect.intersects(collRect))
                return false;
        }
        for(String id : scene.getThingMap("THING_PLAYER").keySet()) {
            if(this.id.equals(id))
                continue;
            if (willCollideWithPlayerAtCoords(scene.getThingMap("THING_PLAYER").get(id), dx, dy)) {
                collidedPlayer = scene.getThingMap("THING_PLAYER").get(id);
                return false;
            }
        }
        return true;
    }

    public boolean willCollideWithPlayerAtCoords(gThing target, int dx, int dy) {
        if(target != null ) {
            return new Rectangle2D.Double(
                    target.coords[0] + target.dims[0]/4,
                    target.coords[1] + target.dims[1]/4,
                    target.dims[0]/2,
                    target.dims[1]/2
            ).intersects(new Rectangle2D.Double(dx,dy,dims[0],dims[1]));
        }
        return false;
    }

    public void checkSpriteFlip() {
        boolean a00 = (fv >= 7*Math.PI/4 && fv <= 9*Math.PI/4);
        boolean a03 = (fv >= 1*Math.PI/4 && fv <= 3*Math.PI/4);
        boolean a04 = (fv >= 3*Math.PI/4 && fv <= 5*Math.PI/4);
        boolean a05 = (fv >= 5*Math.PI/4 && fv <= 7*Math.PI/4);

        if(a00 && !spritePath.contains("a00")) {
            setSpriteFromPath(eManager.getPath(String.format("animations/player_%s/a00.png", color)));
        }
        else if(a03 && !spritePath.contains("a03")) {
            setSpriteFromPath(eManager.getPath(String.format("animations/player_%s/a03.png", color)));
            gWeapons.fromCode(weapon).dims[1] = gWeapons.fromCode(weapon).flipdims[1];
            gWeapons.fromCode(weapon).setSpriteFromPath(gWeapons.fromCode(weapon).spritePath);
        }
        else if(a04 && !spritePath.contains("a04")) {
            setSpriteFromPath(eManager.getPath(String.format("animations/player_%s/a04.png", color)));
        }
        else if(a05 && !spritePath.contains("a05")) {
            setSpriteFromPath(eManager.getPath(String.format("animations/player_%s/a05.png", color)));
            String flippedSprite = gWeapons.fromCode(weapon).spritePath.replace(".png", "_flip.png");
            if(gWeapons.fromCode(weapon) != null) {
                gWeapons.fromCode(weapon).dims[1] = gWeapons.fromCode(weapon).flipdims[0];
                gWeapons.fromCode(weapon).setSpriteFromPath(flippedSprite);
            }
        }
    }

    public void setSpriteFromPath(String newpath) {
        spritePath = newpath;
        sprite = gTextures.getGScaledImage(spritePath, dims[0], dims[1]);
    }

    public gPlayer(String playerId, int x, int y) {
        super();
        type = "THING_PLAYER";
        id = playerId;
        coords[0] = x;
        coords[1] = y;
        dims[0] = 200;
        dims[1] = 200;
        setSpriteFromPath(eManager.getPath("animations/player_teal/a03.png"));
        gPlayer parent = this;
        args.putArg(new gArg("decorationsprite", parent.decorationSprite) {
            public void onChange() {
                parent.decorationSprite = value;
            }
            public String getValue() {
                return parent.decorationSprite;
            }
        });
        args.putArg(new gArg("weapon", Integer.toString(parent.weapon)) {
            public void onChange() {
                parent.weapon = Integer.parseInt(value);
            }
            public String getValue() {
                return Integer.toString(parent.weapon);
            }
        });
        args.putArg(new gArg("attack_target_type", "THING_PLAYER") {
            public void onChange() {
                attackTargetType = value;
            }
            public String getValue() {
                return attackTargetType;
            }
        });
        args.putArg(new gArg("attack_target_id", "null") {
            public void onChange() {
                attackTargetId = value;
            }
            public String getValue() {
                return attackTargetId;
            }
        });
    }
}
