"""
pathfinder.py, by Phil Grim
    Implements an animated demonstration of pathfinding with classical
    graph search algorithms.
    
    All classes are in one file for ease of distribution.



Requires the textbook libraries from 'Introduction to Programming in Python: An Interdisciplinary Approach, 1st ed.'
by Sedgewick, Wayne, and Dondero, which can be found on the book's website at 
https://introcs.cs.princeton.edu/python/code/introcs-python.zip

Outstanding issues:
    Depth-first search gets stuck in the corner
    Breadth-first search never finishes
   
Possible improvements:
    Add entities from interface
    Reset and restart sim
  
Possible practical uses:
  As a lab assignment/take-home exam for students to implement. 
  
References:
  'Introduction to Programming in Python: An Interdisciplinary Approach, 1st ed.'
by Sedgewick, Wayne, and Dondero

  
"""
from collections import deque
from color import Color
from instream import InStream
import heapq
import networkx as nx
import math
import matplotlib.pyplot as plt
import stddraw
import stdio
import sys
import time

"""
Class to represent a directed weighted graph.  
"""
class WeightedGraph:
    def __init__(self):
        self.graph = {}

    def add_vertex(self, vertex):
        if vertex not in self.graph:
            self.graph[vertex] = []

    def add_edge(self, from_vertex, to_vertex, weight):
        if from_vertex in self.graph and to_vertex in self.graph:
            self.graph[from_vertex].append((to_vertex, weight))

    def get_neighbors(self, vertex):
        if vertex in self.graph:
            return self.graph[vertex]
        else:
            return []
     
    
    def dfs_path(self, start_vertex, end_vertex, visited=None):
        """
        Uses depth-first search to find a path.  Doesn't take weights into account.
        """
        if visited is None:
            visited = set()

        if start_vertex not in self.graph or end_vertex not in self.graph:
            return []

        visited.add(start_vertex)

        if start_vertex == end_vertex:
            return [start_vertex]

        for neighbor, _ in self.graph[start_vertex]:
            if neighbor not in visited:
                path = self.dfs_path(neighbor, end_vertex, visited)
                if path:
                    return [start_vertex] + path

        return []    
        
    def shortest_path(self, start_vertex, end_vertex, alg='dijkstra'):
        """
        Find the shortest path between two vertices, using the 
        specified algorithm (one of Dijkstra, Bellman-Ford, and BFS)
        """
        if alg.lower() == 'bfs':
            return self._bfs_shortest_path(start_vertex, end_vertex)
        else:
            if alg.lower() == 'dijkstra':
                distances, previous = self._dijkstra_shortest_path(start_vertex)
            else:
                distances, previous = self._bellman_ford_shortest_path(start_vertex)
                
            if end_vertex not in distances:
                return []

            path = []
            while end_vertex:
                path.insert(0, end_vertex)
                end_vertex = previous.get(end_vertex)

            return path
        
    def _dijkstra_shortest_path(self, start_vertex):
        if start_vertex not in self.graph:
            return None

        # Initialize the distance dictionary with infinity for all vertices 
        # except the start vertex.
        distances = {vertex: float('inf') for vertex in self.graph}
        distances[start_vertex] = 0

        # Priority queue to store vertices to explore.
        priority_queue = [(0, start_vertex)]

        # Dictionary to keep track of the previous vertex in the shortest path.
        previous = {}

        while priority_queue:
            current_distance, current_vertex = heapq.heappop(priority_queue)

            # Skip if we have already processed this vertex.
            if current_distance > distances[current_vertex]:
                continue

            for neighbor, weight in self.graph[current_vertex]:
                distance = current_distance + weight
                if distance < distances[neighbor]:
                    distances[neighbor] = distance
                    previous[neighbor] = current_vertex
                    heapq.heappush(priority_queue, (distance, neighbor))

        return distances, previous
    
    def _bellman_ford_shortest_path(self, start_vertex):
        if start_vertex not in self.graph:
            return None

        # Initialize distances and predecessors
        distances = {vertex: float('inf') for vertex in self.graph}
        predecessors = {vertex: None for vertex in self.graph}
        distances[start_vertex] = 0

        # Relaxation step for each edge
        for _ in range(len(self.graph) - 1):
            for from_vertex in self.graph:
                for to_vertex, weight in self.graph[from_vertex]:
                    if distances[from_vertex] + weight < distances[to_vertex]:
                        distances[to_vertex] = distances[from_vertex] + weight
                        predecessors[to_vertex] = from_vertex

        # Check for negative cycles
        for from_vertex in self.graph:
            for to_vertex, weight in self.graph[from_vertex]:
                if distances[from_vertex] + weight < distances[to_vertex]:
                    raise ValueError("Graph contains a negative cycle")

        return distances, predecessors
    
    def _bfs_shortest_path(self, start_vertex, end_vertex):
        if start_vertex not in self.graph or end_vertex not in self.graph:
            return []

        visited = set()
        queue = deque()
        queue.append((start_vertex, [start_vertex]))  # (current_vertex, path_so_far)

        while queue:
            current_vertex, path = queue.popleft()
            visited.add(current_vertex)

            if current_vertex == end_vertex:
                return path

            for neighbor, _ in self.graph[current_vertex]:
                if neighbor not in visited:
                    new_path = path + [neighbor]
                    queue.append((neighbor, new_path))

        return []

    def plot(self):
        # Create a NetworkX graph
        G = nx.Graph()

        # Add nodes and edges from the WeightedGraph to the NetworkX graph
        for vertex, neighbors in self.graph.items():
            G.add_node(vertex)
        for neighbor, weight in neighbors:
            G.add_edge(vertex, neighbor, weight=weight)

        # Define the layout for the graph (you can change this to suit your needs)
        layout = nx.random_layout(G)

        # Extract edge weights to label edges
        edge_labels = {(u, v): w["weight"] for u, v, w in G.edges(data=True)}

        # Draw the graph with labels
        nx.draw_networkx(G, layout, with_labels=True, node_size=512, 
                node_color="lightblue", font_size=8)
        nx.draw_networkx_edge_labels(G, layout, edge_labels=edge_labels)

        plt.title("Weighted Graph Visualization")
        plt.show()
    
    def __str__(self):
        result = ""
        for vertex, neighbors in self.graph.items():
            result += f"{vertex} -> {neighbors}\n"
        return result


class World:
    """
    Class to represent the world map.
    Populated from a CSV file containing a matrix of edge weights
    """
    def __init__(self, filename):
        # Load the matrix from the csv file
        loader = InStream(filename)
        self._matrix = []
        while loader.hasNextLine():
            line = loader.readLine()
            row = []
            vals = line.split(',')
            for val in vals:
                row.append(int(val))
            self._matrix.append(row)
            
        # Now build the graph from the matrix    
        self._graph = WeightedGraph()
            
        # Build all of the vertices first    
        rowIdx = 0
        while rowIdx < len(self._matrix):
            colIdx = 0
            while colIdx < len(self._matrix[rowIdx]):
                # Build the vertex
                vert = f'({colIdx}, {rowIdx})'
                self._graph.add_vertex(vert)
                colIdx += 1
            rowIdx += 1
                
        # Now build the edges        
        rowIdx = 0
        while rowIdx < len(self._matrix):
            colIdx = 0
            while colIdx < len(self._matrix[rowIdx]): 
                vert = f'({colIdx}, {rowIdx})'
                # North
                if rowIdx > 0:
                    nidx = rowIdx - 1
                    northWeight = self._matrix[nidx][colIdx]
                    northVert = f'({colIdx}, {nidx})'
                    self._graph.add_edge(vert, northVert, northWeight)
                # Northeast
                if rowIdx > 0 and colIdx < len(self._matrix[rowIdx]) - 1:
                    nidx = rowIdx - 1
                    eidx = colIdx + 1
                    northeastWeight = self._matrix[nidx][eidx]
                    northeastVert = f'({eidx}, {nidx})'
                    self._graph.add_edge(vert, northeastVert, northeastWeight)   
                # East
                if colIdx < len(self._matrix[rowIdx]) - 1:
                    eidx = colIdx + 1
                    eastWeight = self._matrix[rowIdx][eidx]
                    eastVert = f'({eidx}, {rowIdx})'
                    self._graph.add_edge(vert, eastVert, eastWeight)
                # Southeast
                if rowIdx < len(self._matrix) - 1 and colIdx < len(self._matrix[rowIdx]) - 1:
                    sidx = rowIdx + 1
                    eidx = colIdx + 1
                    southeastWeight = self._matrix[sidx][eidx]
                    southeastVert = f'({eidx}, {sidx})'
                    self._graph.add_edge(vert, southeastVert, southeastWeight)    
                # South
                if rowIdx < len(self._matrix) - 1:
                    sidx = rowIdx + 1
                    southWeight = self._matrix[sidx][colIdx]
                    southVert = f'({colIdx}, {sidx})'
                    self._graph.add_edge(vert, southVert, southWeight)
                # Southwest
                if rowIdx < len(self._matrix) - 1 and colIdx > 0:
                    sidx = rowIdx + 1
                    widx = colIdx - 1
                    southwestWeight = self._matrix[sidx][widx]
                    southwestVert = f'({widx}, {sidx})'
                    self._graph.add_edge(vert, southwestVert, southwestWeight)
                # West
                if colIdx > 0:
                    widx = colIdx - 1
                    westWeight = self._matrix[rowIdx][widx]
                    westVert = f'({widx}, {rowIdx})'
                    self._graph.add_edge(vert, westVert, westWeight)
                # Northwest
                if rowIdx > 0 and colIdx > 0:
                    nidx = rowIdx - 1
                    widx = colIdx - 1
                    northwestWeight = self._matrix[nidx][widx]
                    northwestVert = f'({widx}, {nidx})'
                    self._graph.add_edge(vert, northwestVert, northwestWeight)
               
                colIdx += 1
            rowIdx += 1
            
    def shortestPath(self, start_vertex, end_vertex, alg):
        if alg.lower() == 'dfs':
            return self._graph.dfs_path(start_vertex, end_vertex)
        else:
            return self._graph.shortest_path(start_vertex, end_vertex, alg)
    
    def cost(self, vertex):
        p = vertex.replace('(', ' ').replace(')', ' ').strip()
        p = p.split(',')
        return self._matrix[int(p[1])][int(p[0])]
                    
    def plotGraph(self):
        self._graph.plot()
        
    def printGraph(self):
        stdio.writeln(self._graph)
        
    def draw(self, left, top, tileSize, showWeights = False):
        # loop over matrix and draw rectangles for each node.
        rowIdx = 0
        while rowIdx < len(self._matrix):
            colIdx = 0
            while colIdx < len(self._matrix[rowIdx]):
                weight = self._matrix[rowIdx][colIdx]
                color = self._mapColor(weight)
                x = left + (colIdx * tileSize)
                y = top - ((rowIdx + 1) * tileSize) # matrix is top-down
                stddraw.setPenColor(color)
                stddraw.filledRectangle(x, y, tileSize, tileSize)
                if showWeights:
                    x += tileSize//2
                    y += tileSize//2
                    if weight in [1, 6, 25]:  # darker colors use white
                        stddraw.setPenColor(stddraw.WHITE)
                    else:                     # lighter colors use black
                        stddraw.setPenColor(stddraw.BLACK)
                    stddraw.setFontSize(round(tileSize * 0.7))
                    stddraw.text(x, y, f'{weight}')            
                colIdx += 1
            rowIdx += 1
                
    def _mapColor(self, weight):
        """
        Maps the cost of the terrain to it's type color
        """
        if   weight == 1:  return stddraw.BLACK
        elif weight == 2:  return Color(0x8B, 0x45, 0x13) # saddle brown
        elif weight == 3:  return Color(0x7C, 0xFC, 0x00) # lawn green
        elif weight == 5:  return Color(0xDA, 0xA5, 0x20) # goldenrod
        elif weight == 6:  return Color(0x00, 0x64, 0x00) # dark green
        elif weight == 7:  return Color(0xEE, 0xDD, 0x82) # light goldenrod
        elif weight == 20: return Color(0x1E, 0x90, 0xFF) # dodger blue
        elif weight == 25: return Color(0x5C, 0x5C, 0x5C) # grey 36
        else:              return stddraw.WHITE
    
class Entity:
    """
    Class to represent a movable entity.
    """
    
    def __init__(self, name, start, end, alg, color = stddraw.WHITE):
        self._name = name
        self._start = start
        self._end = end
        self._alg = alg
        self._color = color
        self._current = start
        self._goal = start
        self._path = []
        self._runs = 0
        self._totalTime = 0.0
        self._avgTime = 0.0
        self._cost = 0
        
        # Give a starting actual position based on the current node
        # move() will update these
        self._x, self._y = self._coords(self._current)
        stdio.writeln(f'({self._x},{self._y})')
        
    def _coords(self, vertex):
        p = vertex.replace('(', ' ').replace(')', ' ').strip()
        p = p.split(',')
        return (int(p[0]), int(p[1]))
        
    def _findPath(self, world):
        stdio.writeln(f'Finding path for {self._name}')
        go = time.time()
        path = world.shortestPath(self._current, self._end, self._alg)
        stop = time.time()
        self._totalTime += stop - go
        self._runs += 1
        self._avgTime = self._totalTime / self._runs
        if len(path) > 1:
            return path[1]
        elif len(path) == 1:
            return path[0]
        else:
            return None
    
    def move(self, world, tileSize):
        """
        Moves the entity one unit towards the current goal.
        If the current goal is reached, calculate the next goal.
        """
        # Just say no, if we're already at the end goal
        if self.goalReached(): return

        self._current = self._goal
        self._x, self._y = self._coords(self._current)
        # Check again, since this could be the step that did it.
        if self.goalReached(): return
        self._goal = self._findPath(world)
        stdio.writeln(f'{self._name} moving to {self._goal}')
        self._path.append(self._goal)
        cost = world.cost(self._goal)
        self._cost += cost          
                    
    def goalReached(self):
        stdio.writeln(f'Checking {self._name}: Current: {self._current}, Goal: {self._end}')
        return self._current == self._end
        
    def draw(self, left, top, tileSize):
        # adjust the x and y coordinates for the offset, get center of tile
        x = (left + (self._x * tileSize)) + tileSize // 2
        y = (top - (self._y * tileSize)) - tileSize // 2
        
        # now draw 
        ctr = y + 3 * (tileSize // 8)
        stddraw.setPenColor(self._color)
        stddraw.filledCircle(x, ctr, tileSize//4 - 1)
        lt = y + tileSize // 4
        lb = y - tileSize // 4
        stddraw.line(x, lb, x, lt)
        la = x - tileSize // 2 + 1
        ra = x + tileSize // 2 - 1
        stddraw.line(la, y, ra, y)
        llx = x - tileSize // 3
        rlx = x + tileSize // 3
        stddraw.line(x, lb, llx, y - tileSize//2)
        stddraw.line(x, lb, rlx, y - tileSize//2)
        
    def drawReport(self, left, top, width, height):
        stddraw.setPenColor(stddraw.BLACK)
        stddraw.filledRectangle(left, top-height, width, height)
        stddraw.setPenColor(stddraw.WHITE)
        stddraw.setPenRadius()
        stddraw.rectangle(left, top-height, width, height)
        lines = round(height/6)
        stddraw.setFontSize(lines - 2)
        stddraw.setPenColor(self._color)
        center = (width + left) // 2 + lines // 2
        # First line has name
        line = 1       
        stddraw.text(center, top - (line * lines), self._name)
        # second line has algorithm
        line = 2
        stddraw.text(center, top - (line * lines), self._alg)
        # third line has number of runs
        line = 3
        stddraw.text(center, top - (line * lines), f'Runs: {self._runs}')
        # fourth line has average time
        line = 4
        stddraw.text(center, top - (line * lines), f'Mean Time: {self._avgTime:.4f}')
        # fifth line has path cost
        line = 5
        stddraw.text(center, top - (line * lines), f'Cost: {self._cost}')
        
class Button:

    """A button is a labeled rectangle in a window.
    It is activated or deactivated with the activate()
    and deactivate() methods. The clicked(p) method
    returns true if the button is active and p is inside it."""

    def __init__(self, x, y, width, height, label):
        """ 
        Creates a rectangular button with the given text label
        """ 

        w,h = width/2.0, height/2.0
        self._x = x
        self._y = y
        self._xmax, self._xmin = x+w, x-w
        self._ymax, self._ymin = y+h, y-h
        self._width = width
        self._height = height
        self._label = label
        self._active = True

    def clicked(self, x, y):
        "Returns true if button active and p is inside"
        return (self._active and
                self._xmin <= x <= self._xmax and
                self._ymin <= y <= self._ymax)

    def draw(self):
        if self._active:
            stddraw.setPenColor(Color(0x70, 0x80, 0x90))
            stddraw.filledRectangle(self._xmin, self._ymin, self._width, self._height)
            stddraw.setPenColor(stddraw.BLACK)
            stddraw.setPenRadius(0.01)
            stddraw.rectangle(self._xmin, self._ymin, self._width, self._height)
            stddraw.setPenRadius()
        else:
            stddraw.setPenColor(stddraw.DARK_GRAY)
            stddraw.filledRectangle(self._xmin, self._ymin, self._width, self._height)
            stddraw.setPenColor(stddraw.BLACK)
            stddraw.rectangle(self._xmin, self._ymin, self._width, self._height)
            
        stddraw.setFontSize(round(self._height * 0.6))
        stddraw.text(self._x, self._y, self._label)
    
    def activate(self):
        "Sets this button to 'active'."
        self._active = True

    def deactivate(self):
        "Sets this button to 'inactive'."
        self._active = False


class Pathfinder:
    """
    Class to implement the pathfinding demo
    """
    # Canvas size
    SCREEN_WIDTH = 1000
    SCREEN_HEIGHT = 900
    FRAME_RATE=1
    LEFT_OFFSET = SCREEN_WIDTH // 5
    BOTTOM_OFFSET = SCREEN_HEIGHT // 10
    TILE_SIZE = (SCREEN_HEIGHT - BOTTOM_OFFSET) // 32
    BUTTON_HEIGHT = SCREEN_HEIGHT // 20
    BUTTON_WIDTH = BUTTON_HEIGHT * 4
    BUTTON_MARGIN = BUTTON_WIDTH // 10
    REPORT_MARGIN = LEFT_OFFSET // 10
    REPORT_WIDTH = LEFT_OFFSET - (2 * REPORT_MARGIN)
    REPORT_HEIGHT = SCREEN_HEIGHT // 8
    
    def __init__(self, filename = 'map.csv'):
        self._simRunning = False
        self._simStart = 0.0
        self._simEnd = 0.0
        self._showWeights = False
        self._world = World(filename)
        self._entities = []
        
        
        e = Entity("Alice", '(2, 28)', '(19, 7)', 'Dijkstra', stddraw.WHITE)
        self._entities.append(e)
        
        e = Entity("Bob", '(30, 2)', '(2, 28)', 'Bellman-Ford', stddraw.RED)
        self._entities.append(e)
        
        #e = Entity("Cindy", '(31, 28)', '(0, 6)', 'BFS', stddraw.BLUE)
        #self._entities.append(e)
        
        e = Entity("David", '(3, 0)', '(30, 22)', 'DFS', stddraw.YELLOW)
        self._entities.append(e)
        
        buttonY = self.SCREEN_HEIGHT // 20
        buttonX = self.SCREEN_WIDTH - (self.SCREEN_WIDTH // 6)
        self._quitButton = Button(buttonX, buttonY, self.BUTTON_WIDTH, 
                                  self.BUTTON_HEIGHT, "Quit")
        buttonX = buttonX - (self.BUTTON_WIDTH + self.BUTTON_MARGIN)
        self._weightButton = Button(buttonX, buttonY, self.BUTTON_WIDTH, 
                                    self.BUTTON_HEIGHT, "Weights")
        buttonX = buttonX - (self.BUTTON_WIDTH + self.BUTTON_MARGIN)
        self._startButton = Button(buttonX, buttonY, self.BUTTON_WIDTH, 
                                   self.BUTTON_HEIGHT, "Start")
        
    
    
        
    def test(self):
        stdio.writeln("Test")
        self._world.printGraph()
        self._world.plotGraph()
        
    def run(self):
        stdio.writeln("Run")
        stddraw.setCanvasSize(self.SCREEN_WIDTH, self.SCREEN_HEIGHT)
        stddraw.setXscale(0, self.SCREEN_WIDTH)
        stddraw.setYscale(0, self.SCREEN_HEIGHT)
        
        self._moreElectricity = True
        
        # Main loop
        while self._moreElectricity:
            self._draw()
            if self._simRunning:
                goals = 0
                for e in self._entities:
                    e.move(self._world, self.TILE_SIZE)
                    if e.goalReached(): goals += 1
                # Stop the clock if all entities have reached their goals
                if goals == len(self._entities): 
                    self._simRunning = False
                    self._simEnd = time.time()
                    
            if stddraw.mousePressed():
                mX = stddraw.mouseX()
                mY = stddraw.mouseY()
                if self._quitButton.clicked(mX, mY):
                    self._moreElectricity = False
                if self._weightButton.clicked(mX, mY):
                    self._showWeights = not self._showWeights
                if self._startButton.clicked(mX, mY):
                    self._startSim()
                    self._startButton.deactivate()
                    
    
    def _startSim(self):
        self._simRunning = True
        self._simStart = time.time()

    def _draw(self):
        stddraw.clear(stddraw.LIGHT_GRAY)
        self._world.draw(self.LEFT_OFFSET, self.SCREEN_HEIGHT, 
                         self.TILE_SIZE, self._showWeights)
        self._quitButton.draw()
        self._weightButton.draw()
        self._startButton.draw()
        
        index = 0
        
        for e in self._entities:
            e.draw(self.LEFT_OFFSET, self.SCREEN_HEIGHT, 
                         self.TILE_SIZE)
            e.drawReport(self.REPORT_MARGIN, self.SCREEN_HEIGHT - (index * self.REPORT_HEIGHT) -
                         (index + 1) * self.REPORT_MARGIN, self.REPORT_WIDTH, self.REPORT_HEIGHT)
            
            index += 1
        
        
        # Draw the elapsed time box
        stddraw.setPenColor(stddraw.WHITE)
        stddraw.filledRectangle(self.REPORT_MARGIN, self.SCREEN_HEIGHT // 20 - self.BUTTON_HEIGHT // 2, 
                                self.BUTTON_WIDTH, self.BUTTON_HEIGHT)
        stddraw.setPenColor(stddraw.BLACK)
        stddraw.rectangle(self.REPORT_MARGIN, self.SCREEN_HEIGHT // 20 - self.BUTTON_HEIGHT // 2, 
                                self.BUTTON_WIDTH, self.BUTTON_HEIGHT)
        elapsed = 0.0
        if self._simRunning:
            elapsed = time.time() - self._simStart
        else:
            elapsed = self._simEnd - self._simStart
                 
        stddraw.text(self.REPORT_MARGIN + self.BUTTON_WIDTH // 2, self.SCREEN_HEIGHT // 20, 
                     f'Elapsed Time: {elapsed:.3f}')
        
        stddraw.show(self.FRAME_RATE)

if __name__ == '__main__':
    
    pathfinder = Pathfinder('map.csv')
    
    if len(sys.argv) == 2 and sys.argv[1] == '-test':
        pathfinder.test();
    else:    
        pathfinder.run();
