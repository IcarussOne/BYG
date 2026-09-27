import React from 'react';
import styles from './styles.module.css';

// Screenshots live in src/assets/biomes/<registry_id>.png (e.g. byg_alps.png).
// Drop a file in and it shows up; until then a placeholder box is rendered.
declare const require: {
  context: (dir: string, deep: boolean, filter: RegExp) => {
    keys: () => string[];
    (key: string): string | {default: string};
  };
};

const shots = require.context('../../assets/biomes', false, /\.png$/);

function findShot(id: string): string | undefined {
  const key = `./${id}.png`;
  if (!shots.keys().includes(key)) {
    return undefined;
  }
  const mod = shots(key);
  return typeof mod === 'string' ? mod : mod.default;
}

export default function BiomeScreenshot({id, name}: {id: string; name: string}) {
  const src = findShot(id);
  if (!src) {
    return (
      <div className={styles.placeholder}>
        Screenshot pending: <code>src/assets/biomes/{id}.png</code>
      </div>
    );
  }
  return (
    <figure className={styles.figure}>
      <img src={src} alt={`${name} biome`} loading="lazy" />
    </figure>
  );
}
