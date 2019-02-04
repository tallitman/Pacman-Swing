package Actors;

import Actors.Pacman.*;
/**
 * interface for know with who has collison 
 * @author user
 *
 */
public interface Visitor {
public void visit(PacmanL1 p);
public void visit(PacmanL2 p);
public void visit(PacmanL3 p);
}
