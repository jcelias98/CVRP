The capacited vehicle routing problem (CVRP) is a famous NP-hard
problem in the literature. The solution is to serve all customers by generating
a route with minimal cost for each vehicle that starts from the same depot. The
vehicles have uniform capacity and need to go back and forth to the depot. Each
customer has a known demand and should receive only one visit from a single
vehicle. In this work we implemented the centroid-based heuristic algorithm to
solve the CVRP in polynomial time. The algorithm consists of three phases:
cluster construction, cluster adjustment and route establishment. The notion
of geometric center guides the first two phases. After the establishment of the
groups the shorter route of each cluster is found. An algorithm based on Simulated Annealing is used to find the best route that each vehicle should follow.

# Author: Juan Carlos Elias Obando Valdivia
