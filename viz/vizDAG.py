import json
import sys
import ntpath

template = '''<!DOCTYPE html>
<html><head>
  <title>Pipeline | <benchmark-name></title>

  <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/vis/4.21.0/vis.min.js"></script>
  <link href="https://cdnjs.cloudflare.com/ajax/libs/vis/4.21.0/vis.min.css" rel="stylesheet" type="text/css">

  <style type="text/css">
    #dag {
      position:fixed;
      padding:0;
      margin:0;

      top:0;
      left:0;

      width: 100%;
      height: 100%;
      border: 1px solid lightgray;
    }
  </style>
</head>
  <body>
    <div id="dag"></div>

    <script type="text/javascript">
      // create an array with nodes
      var nodes = new vis.DataSet(<nodes-data>);

      // create an array with edges
      var edges = new vis.DataSet(<edges-data>);

      // create a network
      var container = document.getElementById('dag');
      var data = {
        nodes: nodes,
        edges: edges
      };
      var options = {
        layout: {
          hierarchical: {
            direction: 'UD', 
            sortMethod: 'directed',
            levelSeparation: 250
          }
        }, 
        nodes: {
          font: {
            size: 16, 
            align: 'left', 
            face: 'courier'
          }, 
          color: {
            background: '#ffffe6'
          },
          fixed: {
            x:false,
            y:false
          }
        }, 
        edges: {
          arrows: {
            to:     {enabled: true, scaleFactor:1, type:'arrow'},
            middle: {enabled: false, scaleFactor:1, type:'arrow'},
            from:   {enabled: false, scaleFactor:1, type:'arrow'}
          }
        },
        physics: false
      };
      var network = new vis.Network(container, data, options);
    </script>
  </body>
</html>'''

# Parse data from JSON
input_file_path = sys.argv[1]
input_file_name = ntpath.basename(input_file_path)
benchmark_name = input_file_name.split(".")[0]

fh = open(input_file_path, "r")
data = json.loads(fh.read())

stages = data["Stages"]
nodes = []
edges = []

# Populate nodes and edges
nextId = 10000
for stage in stages:
  node = {}
  
  node["id"] = stage["id"]
  #node["level"] = stage["level"]
  node["title"] = "Stage " + str(stage["id"])
  node["shape"] = "box"
  if not stage["isEmpty"]:
    node["label"] = stage["code"].encode("ascii")
  else:
    node["label"] = "No-op"
  
  nodes.append(node)

  if (stage["forks"]):
    split_id = nextId
    nextId += 1

    split_node = {}
    split_node["id"] = split_id
    #split_node["level"] = stage["level"] + 1
    split_node["shape"] = "circle"
    split_node["label"] = stage["cond"].encode("ascii")
    nodes.append(split_node)

    edges.append({"from": stage["id"], "to": split_id})
    edges.append({"from": split_id, "to": stage["cons"]})
    edges.append({"from": split_id, "to": stage["altr"]})
  else:  
    edges.append({"from": stage["id"], "to": stage["next"]})

# Add benchmark name as title to template
template = template.replace("<benchmark-name>", benchmark_name)

template = template.replace("<nodes-data>", str(nodes))
template = template.replace("<edges-data>", str(edges))

# Generate html file
fh = open(benchmark_name + ".html", "w")
fh.write(template)
fh.close()