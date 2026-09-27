import React from 'react';
import {tempColor} from '@site/src/utils/tempColor';
import styles from './styles.module.css';

export default function TempBadge({value}: {value: number}) {
  return (
    <span className={styles.badge}>
      <span className={styles.dot} style={{backgroundColor: tempColor(value)}} />
      {value}
    </span>
  );
}
