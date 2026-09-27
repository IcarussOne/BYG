import React, {useMemo, useState} from 'react';
import Link from '@docusaurus/Link';
import biomes from '@site/src/data/biomes.json';
import {tempColor} from '@site/src/utils/tempColor';
import styles from './styles.module.css';

type Biome = (typeof biomes)[number];
type Key = 'name' | 'id' | 'group' | 'climate' | 'defaultWeight' | 'temperature' | 'rainfall' | 'baseHeight' | 'heightVariation';

const columns: {key: Key; label: string; numeric?: boolean}[] = [
  {key: 'name', label: 'Biome'},
  {key: 'id', label: 'ID'},
  {key: 'group', label: 'Group'},
  {key: 'climate', label: 'Climate'},
  {key: 'defaultWeight', label: 'Default weight', numeric: true},
  {key: 'temperature', label: 'Temp.', numeric: true},
  {key: 'rainfall', label: 'Rain', numeric: true},
  {key: 'baseHeight', label: 'Height', numeric: true},
  {key: 'heightVariation', label: 'Variation', numeric: true},
];

export default function BiomeTable() {
  const [sort, setSort] = useState<{key: Key; dir: 1 | -1}>({key: 'name', dir: 1});

  const rows = useMemo(() => {
    const sorted = [...biomes].sort((a: Biome, b: Biome) => {
      const x = a[sort.key];
      const y = b[sort.key];
      const cmp = typeof x === 'number' && typeof y === 'number' ? x - y : String(x).localeCompare(String(y));
      return cmp * sort.dir || a.name.localeCompare(b.name);
    });
    return sorted;
  }, [sort]);

  const toggle = (key: Key) =>
    setSort((s) => (s.key === key ? {key, dir: s.dir === 1 ? -1 : 1} : {key, dir: 1}));

  return (
    <div className={styles.wrapper}>
      <table className={styles.table}>
        <thead>
          <tr>
            {columns.map((c) => (
              <th
                key={c.key}
                aria-sort={sort.key === c.key ? (sort.dir === 1 ? 'ascending' : 'descending') : 'none'}
                className={c.numeric ? styles.numeric : undefined}>
                <button type="button" className={styles.sortButton} onClick={() => toggle(c.key)}>
                  {c.label}
                  <span className={styles.arrow} aria-hidden="true">
                    {sort.key === c.key ? (sort.dir === 1 ? '▲' : '▼') : '↕'}
                  </span>
                </button>
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {rows.map((b) => (
            <tr key={b.id}>
              <td>
                <Link to={`/biomes/${b.slug}#${b.id}`}>{b.name}</Link>
              </td>
              <td>
                <code>{b.id.replace(/^byg_/, '')}</code>
              </td>
              <td>{b.group}</td>
              <td>{b.climate}</td>
              <td className={styles.numeric}>{b.defaultWeight}</td>
              <td className={styles.numeric}>
                <span className={styles.tempCell}>
                  <span className={styles.tempDot} style={{backgroundColor: tempColor(b.temperature)}} />
                  {b.temperature}
                </span>
              </td>
              <td className={styles.numeric}>{b.rainfall}</td>
              <td className={styles.numeric}>{b.baseHeight}</td>
              <td className={styles.numeric}>{b.heightVariation}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
