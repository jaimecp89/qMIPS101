package domain.engine.quantum.maps;

import java.util.Map;
import java.util.Map.Entry;

import domain.engine.math.Complex;
import domain.engine.quantum.ClassicState;

public interface IterableMap extends Map<ClassicState, Complex>, Iterable<Entry<ClassicState, Complex>>{}
