# Report for assignment 3


## Project

Name: Spatial4j

URL: https://github.com/locationtech/spatial4j

Spatial4j is a general purpose spatial / geospatial ASL licensed open-source Java library. It's core capabilities are 3-fold: to provide common shapes that can work in Euclidean and geodesic (surface of sphere) world models, to provide distance calculations and other math, and to read & write shapes from formats like WKT and GeoJSON. 

## Onboarding experience

Did it build as documented?
    
No issues with the onboarding at all. Just cloning the repo and then importing the project into an IDE like intelliJ did the trick
for most of the members of the group. Just be sure to use JDK 1.8, otherwise some tests will fail.


## Complexity

| Function                                           | CCN (lizard) | CCN (Manual) | Lines of Code |
|----------------------------------------------------|:------------:|-----------:|--------------|
| PolyshapeWriter::write                             |      13      |     13     |      30      |
| JtsGeometry::unwrapDateline                        |      11      |     11     |      45      |
| GeoCircle::relateRectangleCircleWrapsPol           |      13      |     12     |      38      |
| GeoJSONReader::readDistance                        |      11      |      9     |      30      |
| JtsGeoJSONWriter::write                            |      14      |     16     |      59      |
| CartesianDistCalc::distanceToLineSegment           |       4      |            |      22      |
| DistanceUtils::calcBoxByDistFromPt_latHorizAxisDEG |       7      |            |      18      |
| Range::LongitudeRange::expandTo                    |       7      |            |      17      |
| ShapeCollection<S::computeMutualDisjoint           |       4      |            |      11      |
| GeodesicSphereDistCalc::pointOnBearing             |       3      |            |      14      |

### Functions
The documentation for individual functions in spatial4j is highly limited and varied. Some functions have descriptions of both the function and the return type/effect but others are entirely lacking documentation. Few functions give detailed description of all possible outcomes.

#### PolyshapeWriter::write
The `write` function takes a encoder object and a shape object. It encodes a shape as an ASCII string using the Polyshape format.

#### Geometry::unwrapDateline
The `unwrapDateline` function takes a geometry object and if it spans the meridian it will unwrap it so that it no longer does so. This is needed because of geographic representation implementations.

#### GeoCircle::relateRectangleCircleWrapsPol
The `relateRectangleCircleWrapsPol` function takes a rectangle object and will return a spatial relation describing it's relation with a pole. That is if the pole is f.i. contained within the shape or other relations.

#### GeoJSONReader::readDistance   
The `readDistance` takes a JSON parser and reads data from it until a certain distance has been found.

#### JtsGeoJSONWriter::write  
The `write` function takes a writer object and a geometry object. It will will append a textual representation of the geometry object to the writer which describes the objects type e.g. _MultiPolygon_ and a list of coordinate points e.g. _[[1.0,1.0],[1.0,2.0]]_. GeoJSON is a specific type of format for describing geographic objects using JSON.

### Analysis
The complexity measurement tool used for the analysis is Lizard. The manual calculations of cyclomatic complexity is based on the manually constructed condensation graphs (see report/condensation_graphs/).

The result of the manual calculations were coherent within the group but occasionally deviated from the result given by lizard. The results are similar but deviate by up to 2. This seems to indicate that the methods of calculations are similar but that lizard considers some combinations of language features differently than we do. We use a language independent approach whilst lizard has specific features for Java. There wasn't many instances of exceptions in the chosen functions so we have no indication on the handling of them by the tool.

The length of the chosen functions are included in the table above. There seems to be no simple relation between length and complexity. Whilst the longer functions are in general more complex than the shorter functions there is exceptions like the functions `relateRectangleCircleWrapsPol` and `unwrapDateline`.

## Coverage

### Tools

Document your experience in using a "new"/different coverage tool.

As a different coverage tool we used the built in tool within IntelliJ IDEA. With this 
we could easily identify functions that did not branch fully. We could not directly 
identify lack of branch coverage, but instead general coverage. Such as methods not being called, 
classes not being tested and overall line coverage. From that we could easily find functions which lacked branch coverage.

### DYI

Show a patch that show the instrumented code in main (or the unit
test setup), and the ten methods where branch coverage is measured.

The patch is probably too long to be copied here, so please add
the git command that is used to obtain the patch instead:

git diff ...

What kinds of constructs does your tool support, and how accurate is
its output?

### Evaluation
Some functions from the initial list have been removed due to having dead code and such.
##### Report of old coverage: 

* JSON::write: (9/14) 64.3%
* unwrapDateline: (8/12) 66.7%
* relateRectangleCircleWrapsPol: (10/13) 92.3%
* polyShapeWrite: (10/13) 76.9%
* pointOnBearing: (2/3) 66.7%
* DistanceToLineSegment: (4/5) 80.0%
* expandTo: (0/7) 0%
* computeMutualDisjoint: (0/4) 0%
* DistanceUtils::vectorDistance: (0/5) 0%
* CartesianDistCalc::pointOnBearing: (4/6) 66.7%
* distLawOfCosinesRAD (4/5) 80.0%
* CartesianDistCalc::equals: (1/4) 25%
* CircleImpl::relate: (8/9) 88.9%


##### Report of new coverage: 

* JSON::write: (9/14) 64.3%
* unwrapDateline: (8/12) 66.7%
* relateRectangleCircleWrapsPol: (10/13) 92.3%
* polyShapeWrite: (10/13) 76.9%
* pointOnBearing: (<span style="color:red">**3**</span>/3) 100%
* DistanceToLineSegment: (<span style="color:red">**5**</span>/5) 100.0%
* expandTo: (0/7) 0%
* computeMutualDisjoint: (<span style="color:red">**4**</span>/4) 100%
* DistanceUtils::vectorDistance: (<span style="color:red">**5**</span>/5) 100%
* CartesianDistCalc::pointOnBearing: (<span style="color:red">**6**</span>/6) 100%
* distLawOfCosinesRAD (<span style="color:red">**5**</span>/5) 100%
* CartesianDistCalc::equals: (<span style="color:red">**3**</span>/4) 75%
* CircleImpl::relate: (<span style="color:red">**9**</span>/9) 100%


New branches: 14

#### Test cases added:

##### Jenny:

`testdistanceToLineSegment()`

`testDistCalcPointOnBearing_cartesian_reuse()`

##### Jakob: 

`testComputeMutualDisjointEmpty()`

`testComputeMutualDisjointRectangles()`

##### Philip:

`testVectorDistance()`

`testDistCalcPointOnBearing_reuse()`

##### Shiva:

` test_distLawOfCosinesRAD()`

##### Fredrik:

`testEquals()`

`testRelate()`

## Refactoring

Plan for refactoring complex code:

In functions such as DistanceUtils::calcBoxByDistFromPt_latHorizAxisDEG , there is dead code that is never reached 
without which the branch coverage would be quite higher otherwise. This dead code includes branches that are never entered 
and thus reduce branch coverage. Therefore removing unreachable branches would both improve complexity as well as branch coverage. 

As well as avoiding dead branches, avoiding branch clashes such as if-statements whose conditions are captured by other if-statements 
in the same function could be another way to reduce complexity. 

## Statement of contribution 

For each team member, how much time was spent in

Shiva: Contributed to early identification of complex functions as well as the required complexity analysis and evaluations. Added test cases to improved branch coverage of a specific function DistanceUtils:: DistLawsOfCosineRAD from 80% to 100%. Contributed to the report written in terms of responding to reflection questions and analysis. Added functionality to several functions in order to measure their branch coverage for the purpose of analyzing whether the branch coverhge can be improved. 

Jenny: Part 0: Early identification and evaluation of potential open source projects (including loc checking and test coverage). Pt 1: Selection of functions, automatic and manual calculation of function complexity, condensation graphs, description of said functions and complexity questions in report. Part 2: Two new tests to improve coverage (see test cases added).

Jakob: Part 2: DIY (Ad hoc branch coverage tool together with Fredrik), also contributed to report.md and improved branch coverage for computeMutualDisjoint from 0 to 100%, see test cases added. 

Philip: Part 1: Manual calculation of function complexity, condensation graphs. Part 2: Two new tests to improve coverage (GeodesicSphereDistCalc::pointOnBearing from 33 % to 100 % and DistanceUtils::vectorDistance from 0 % to 100 %).

Fredrik: Part 2: DIY (Ad hoc branch coverage tool together with Jakob). Improved branch coverage for CartesianDistCalc::equals from 25% to 75% and CircleImpl::relate from 88.9% to 100%, see test cases added. 

## Overall experience

What are your main take-aways from this project? What did you learn?

Is there something special you want to mention here?

Takeaways: 
- Usage of the tool lizard for the purrpose of complexity analysis and branch covergae. 
- Methods to improve branch coverage as well as complexity 
- The concepts of branch coverage and path coverage
- Writing test cases and test suits for open-source projects specially if there are not our own 
