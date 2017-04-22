# Set Game: Software Implementation of the Game of Set.

Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod
tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,
quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo
consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse
cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non
proident, sunt in culpa qui officia deserunt mollit anim id est laborum.

# Quick overview

**Code Example**
```python
# Classification
tflearn.init_graph(num_cores=8, gpu_memory_fraction=0.5)

net = tflearn.input_data(shape=[None, 784])
net = tflearn.fully_connected(net, 64)
net = tflearn.dropout(net, 0.5)
net = tflearn.fully_connected(net, 10, activation='softmax')
net = tflearn.regression(net, optimizer='adam', loss='categorical_crossentropy')

model = tflearn.DNN(net)
model.fit(X, Y)
```

```python
# Sequence Generation
net = tflearn.input_data(shape=[None, 100, 5000])
net = tflearn.lstm(net, 64)
net = tflearn.dropout(net, 0.5)
net = tflearn.fully_connected(net, 5000, activation='softmax')
net = tflearn.regression(net, optimizer='adam', loss='categorical_crossentropy')

model = tflearn.SequenceGenerator(net, dictionary=idx, seq_maxlen=100)
model.fit(X, Y)
model.generate(50, temperature=1.0)
```

There are many more examples available [here](examples).

# Installation

To install TFLearn, see: [Installation Guide](installation).

# GUI Pictures

**Graph**

![Graph Visualization](./img/graph.png)

**Loss & Accuracy (multiple runs)**

![Loss Visualization](./img/loss_acc.png)

**Layers**

![Layers Visualization](./img/layer_visualization.png)

# Sources

GitHub: [https://github.com/krisht/SoftwareSetGame](https://github.com/krisht/SoftwareSetGame).

# Contributions

This is the only release of our implementation of Software Set Game

# License

MIT License